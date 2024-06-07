package com.example.expensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;

    private FirebaseAuth mAuth;
    private String onlineUserId="";
    private DatabaseReference expensesRef, personalRef;

    private TextView monthRatioSpending, monthSpentAmount,totalBudgetAmountTextView, analyticsTransportAmount,analyticsFoodAmount,analyticsEntertainmentAmount;
    private TextView analyticsHouseExpensesAmount,analyticsEducationAmount,analyticsCharityAmount,analyticsApparelAmount,analyticsHealthAmount,analyticsPersonalExpensesAmount,analyticsOtherAmount;

    private RelativeLayout linearLayoutFood,linearLayoutTransport,linearLayoutHouse,linearLayoutEntertainment,linearLayoutEducation;
    private RelativeLayout linearLayoutCharity,linearLayoutApparel,linearLayoutHealth,linearLayoutPersonalExp,linearLayoutOther,linearLayoutAnalysis;

    private AnyChartView anyChartView;

    private ImageView status_Image_hea,monthRatioSpending_Image,status_Image_transport, status_Image_food, status_Image_house, status_Image_ent,status_Image_edu,status_Image_cha,status_Image_app,status_Image_pexp,status_Image_other,status_Image_analysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        settingsToolbar =findViewById(R.id.toolbar);
        //setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Monthly Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef= FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef= FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);

        //general analytic
        monthSpentAmount = findViewById(R.id.totalBudgetAmountTextView);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_Image = findViewById(R.id.monthRatioSpending_Image);

        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHouseExpensesAmount=findViewById(R.id.analyticsHouseExpensesAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount=findViewById(R.id.analyticsEducationAmount);
        analyticsCharityAmount=findViewById(R.id.analyticsCharityAmount);
        analyticsApparelAmount=findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount=findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalExpensesAmount=findViewById(R.id.analyticsPersonalExpensesAmount);
        analyticsOtherAmount=findViewById(R.id.analyticsOtherAmount);

        //RelativeLayoutViews
        linearLayoutTransport = findViewById(R.id.linearLayoutTransport);
        linearLayoutFood = findViewById(R.id.linearLayoutFood);
        linearLayoutHouse = findViewById(R.id.linearLayoutHouse);
        linearLayoutEntertainment = findViewById(R.id.linearLayoutEntertainment);
        linearLayoutEducation = findViewById(R.id.linearLayoutEducation);
        linearLayoutCharity = findViewById(R.id.linearLayoutCharity);
        linearLayoutApparel = findViewById(R.id.linearLayoutApparel);
        linearLayoutHealth= findViewById(R.id.linearLayoutHealth);
        linearLayoutPersonalExp=findViewById(R.id.linearLayoutPersonalExp);
        linearLayoutOther=findViewById(R.id.linearLayoutOther);

        //imageViews
        status_Image_transport = findViewById(R.id.transport_status);
        status_Image_food= findViewById(R.id.food_status);
        status_Image_house=findViewById(R.id.house_status);
        status_Image_ent=findViewById(R.id.entertainment_status);
        status_Image_edu=findViewById(R.id.education_status);
        status_Image_cha=findViewById(R.id.charity_status);
        status_Image_app=findViewById(R.id.fashion_status);
        status_Image_hea = findViewById(R.id.health_status);
        status_Image_pexp=findViewById(R.id.personal_status);
        status_Image_other=findViewById(R.id.other_status);

        //anyChartView
        anyChartView =  findViewById(R.id.anyChartView);



    }
}