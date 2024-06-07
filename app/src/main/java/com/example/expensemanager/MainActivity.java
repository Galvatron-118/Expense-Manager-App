package com.example.expensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CardView budgetCardView, dailyCardView,historyCardView;
    private ImageView weekBtnImageView,todayBtnImageView, budgetBtnImageView, monthBtnImageView, analyticsImageView;
    private TextView  weeklyTv, budgetTv, dailyTv,monthlyTv,savingsTv;
    
    private FirebaseAuth mAuth;
    private DatabaseReference budgetRef, expensesRef, personalRef;
    private String onlineUserID ="";

    private int totalAmountMonth= 0;
    private int totalAmountBudget =0;
    private int totalAmountBudgetB=0;
    private int totalAmountBudgetC =0;
    private List<Data> myDataList;


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       toolbar = findViewById(R.id.toolbar);
      //setSupportActionBar(toolbar);
     // getSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Expense Manager");


         budgetTv=findViewById(R.id.budgetTv);
         dailyTv=findViewById(R.id.dailyTv);
         savingsTv=findViewById(R.id.savingsTv);
         monthlyTv=findViewById(R.id.monthlyTv);
         weeklyTv=findViewById(R.id.weeklyTv);

         mAuth = FirebaseAuth.getInstance();
         onlineUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
         budgetRef= FirebaseDatabase.getInstance().getReference("budget").child(onlineUserID);
         expensesRef=FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
         personalRef=FirebaseDatabase.getInstance().getReference("personal").child(onlineUserID);




        weekBtnImageView = findViewById(R.id.weekBtnImageView);
        todayBtnImageView = findViewById(R.id.todayBtnImageView);
        budgetBtnImageView = findViewById(R.id.budgetBtnImageView);
        monthBtnImageView= findViewById(R.id.monthBtnImageView);
        analyticsImageView = findViewById(R.id.analyticsImageView);


       /// budgetCardView = findViewById(R.id.budgetCardView);
       /// dailyCardView = findViewById(R.id.dailyCardView);

        historyCardView = findViewById(R.id.historyCardView);
        historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        budgetBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this,BudgetActivity.class);
              startActivity(intent);
            }
        });

        todayBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TodaySpendingActivity.class);
                startActivity(intent);
            }
        });

        weekBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WeekSpendingActivity.class);
                intent.putExtra("type","week");
                startActivity(intent);
            }
        });

        monthBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WeekSpendingActivity.class);
                intent.putExtra("type","month");
                startActivity(intent);
            }
        });

        analyticsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
            }
        });

        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudgetB+=pTotal;
                    }
                    totalAmountBudgetC=totalAmountBudgetB;
                }
                else{
                    personalRef.child("budget").setValue(0);
                    Toast.makeText(MainActivity.this,"Please Set a Budget",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



         getBudgetAmount();
         getTodaySpentAmount();
         getWeekSpentAmount();
         //getMonthSpentAmount();
         //getSavings();





    }




    private void getBudgetAmount() {
        budgetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Map<String,Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmountBudget+=pTotal;
                        budgetTv.setText("$ "+String.valueOf(totalAmountBudget));
                    }

                }
                else{
                    totalAmountBudget=0;
                    budgetTv.setText("$ "+String.valueOf(0));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void getTodaySpentAmount() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        Calendar  cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               int totalAmount=0 ;
               for(DataSnapshot ds : snapshot.getChildren()){
                   Data data = snapshot.getValue(Data.class);
                   Map<String,Object> map = (Map<String, Object>) ds.getValue();
                   Object total = map.get("amount");
                   int pTotal = Integer.parseInt(String.valueOf(total));
                   totalAmount+=pTotal;
                   dailyTv.setText("$ "+totalAmount);
               }
               personalRef.child("today").setValue(totalAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
   /*     DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDataList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Data data = dataSnapshot.getValue(Data.class);
                    myDataList.add(data);
                }

              //  todayItemsAdapter.notifyDataSetChanged();
               // progressBar.setVisibility(View.GONE);

                int totalAmount = 0;
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    Map<String, Object > map= (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");;
                    int pTotal= Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    dailyTv.setText("Total Day's Spending: $"+totalAmount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); */

    }
/*
    public void getMonthSpentAmount() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount =0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(totalAmount));
                    totalAmount+=pTotal;
                    monthlyTv.setText("$ "+totalAmount);
                }
                personalRef.child("month").setValue(totalAmount);
                totalAmountMonth+=totalAmount;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
    public void getWeekSpentAmount() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserID);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount =0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    weeklyTv.setText("$ "+totalAmount);
                }
                personalRef.child("week").setValue(totalAmount);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    public void getSavings() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists())
               {
                   int budget;
                   if(snapshot.hasChild("budget"))
                   {
                       budget = Integer.parseInt(snapshot.child("budget").getValue().toString());
                   }
                   else{
                       budget=0;
                   }
                   int monthSpending;
                   if(snapshot.hasChild("month"))
                   {
                       monthSpending = Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                   }else{
                       monthSpending=0;
                   }

                   int savings = budget - monthSpending;
                   savingsTv.setText("$ "+savings);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.account){
           Intent intent = new Intent(MainActivity.this, AccountActivity.class);
           startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}