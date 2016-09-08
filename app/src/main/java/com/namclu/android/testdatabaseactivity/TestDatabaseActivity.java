package com.namclu.android.testdatabaseactivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Random;

public class TestDatabaseActivity extends ListActivity {
    private CommentsDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        dataSource = new CommentsDataSource(this);
        dataSource.open();

        List<Comment> values = dataSource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1,
                values);

        setListAdapter(adapter);

    }

    // Will be called via the onClick attribute
    // of the buttons in activity_test_database.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>)getListAdapter();

        Comment comment = null;
        switch (view.getId()) {
            case R.id.add:
                String[] comments = new String[] {"Cool", "Very nice", "Hate it"};
                int nextInt = new Random().nextInt(3);

                // save the new comment to the database
                comment = dataSource.createComment(comments[nextInt]);
                adapter.add(comment);
                break;

            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    dataSource.deleteComment(comment);
                    adapter.remove(comment);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
