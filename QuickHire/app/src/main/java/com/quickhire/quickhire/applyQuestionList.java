package com.quickhire.quickhire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;
import java.util.Vector;


public class applyQuestionList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, questionAdapter.MessageAdapterListener {
    private jobPosting post = ApplyActivity.posting;
    private RecyclerView recyclerView;
    private questionAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Integer questionPosition;
    public static String questionText;
    public static int videoTime;
    public static Question selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_question_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (selectedQuestion != null) {
            Toast.makeText(getApplicationContext(), "Update", Toast.LENGTH_SHORT).show();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new questionAdapter(this, post.questions, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

//        actionModeCallback = new ActionModeCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
//        getInbox();
    }

    @Override
    public void onIconClicked(int position) {
//        if (actionMode == null) {
//            actionMode = startSupportActionMode(actionModeCallback);
//        }
//
//        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {

    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
//            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Question question = post.questions.get(position);
            questionPosition = position;
            questionText = question.getQuestionText();
            selectedQuestion = question;
            if (question.getType() == "Essay") {
                startActivityForResult(new Intent(applyQuestionList.this, essayAnswerActivity.class), 1);
            } else if (question.getType() == "Video") {
//                videoTime = ((videoQuestion) question).getTime();
//                startActivityForResult(new Intent(applyQuestionList.this, videoQuestionActivity.class), 2);
            }
            Toast.makeText(getApplicationContext(), "Read: " + question.getType(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowLongClicked(int position) {

    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String result = data.getStringExtra("answer");
            Question question = post.questions.get(questionPosition);
            question.setAnswer(result);
            mAdapter.removeData(questionPosition);
            post.questions.add(questionPosition,question);
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
//            String result = data.getStringExtra("question");
//            String timeString = data.getStringExtra("time");
//            int time = Integer.parseInt(timeString);
//            Question videoQ = new videoQuestion(result, time);
//            mAdapter.removeData(questionPosition);
//            questions.add(videoQ);
//            mAdapter.notifyDataSetChanged();
//            Toast.makeText(getApplicationContext(), timeString, Toast.LENGTH_SHORT).show();
        }
    }
}
//
//    @Override
//    public void onRowLongClicked(int position) {
//        // long press is performed, enable action mode
//        enableActionMode(position);
//    }
//
//    private void enableActionMode(int position) {
//        if (actionMode == null) {
//            actionMode = startSupportActionMode(actionModeCallback);
//        }
//        toggleSelection(position);
//    }
//
//    //
//    private void toggleSelection(int position) {
//        mAdapter.toggleSelection(position);
//        int count = mAdapter.getSelectedItemCount();
//
//        if (count == 0) {
//            actionMode.finish();
//        } else {
//            actionMode.setTitle(String.valueOf(count));
//            actionMode.invalidate();
//        }
//    }
//
//
//    private class ActionModeCallback implements ActionMode.Callback {
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
//
//            // disable swipe refresh if action mode is enabled
//            swipeRefreshLayout.setEnabled(false);
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_delete:
//                    // delete all the selected messages
//                    deleteMessages();
//                    mode.finish();
//                    return true;
//
//                default:
//                    return false;
//            }
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mAdapter.clearSelections();
//            swipeRefreshLayout.setEnabled(true);
//            actionMode = null;
//            recyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter.resetAnimationIndex();
//                    // mAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//    }
//
//    // deleting the messages from recycler view
//    private void deleteMessages() {
//        mAdapter.resetAnimationIndex();
//        List<Integer> selectedItemPositions =
//                mAdapter.getSelectedItems();
//        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
//            mAdapter.removeData(selectedItemPositions.get(i));
//        }
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    protected void onDestroy() {
//        CreateJobPosting2.questionsList = questions;
//        super.onDestroy();
//    }
//}
//
//