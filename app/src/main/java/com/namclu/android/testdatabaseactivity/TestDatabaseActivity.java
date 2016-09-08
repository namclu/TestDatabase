package com.namclu.android.testdatabaseactivity;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.List;

public class TestDatabaseActivity extends ListActivity {
    private CommentsDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        dataSource = new CommentsDataSource(this);
        dataSource.open();

        List 
    }
}
