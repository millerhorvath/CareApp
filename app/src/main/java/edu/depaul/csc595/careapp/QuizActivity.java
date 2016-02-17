package edu.depaul.csc595.careapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    public enum QuestionType {
        CHECKBOX, RADIOBUTTON
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setResult(0);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final TextView questionHeaderText = (TextView) findViewById(R.id.questionHeaderText);

        int printCQ = this.currentQuestion + 1;
        questionHeaderText.setText("QUESTION " + printCQ + " UP TO " + this.questionNumber + ":");

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(this.questionNumber-1);

        questionText = (TextView) findViewById(R.id.questionText);
        questionText.setText(QUESTIONS[this.currentQuestion]);

        checkBox[0] = (CheckBox) findViewById(R.id.checkBox1);
        checkBox[1] = (CheckBox) findViewById(R.id.checkBox2);
        checkBox[2] = (CheckBox) findViewById(R.id.checkBox3);
        checkBox[3] = (CheckBox) findViewById(R.id.checkBox4);
        checkBox[4] = (CheckBox) findViewById(R.id.checkBox5);

        radio[0] = (RadioButton) findViewById(R.id.radioButton1);
        radio[1] = (RadioButton) findViewById(R.id.radioButton2);
        radio[2] = (RadioButton) findViewById(R.id.radioButton3);
        radio[3] = (RadioButton) findViewById(R.id.radioButton4);
        radio[4] = (RadioButton) findViewById(R.id.radioButton5);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

        if(TYPE[0] == QuestionType.CHECKBOX) {
            for(int i = 0; i < 5; i++) {
                checkBox[i].setText(ANSWERS[0][i]);
                radio[i].setVisibility(View.INVISIBLE);
            }
        } else {
            for(int i = 0; i < 5; i++) {
                radio[i].setText(ANSWERS[0][i]);
                checkBox[i].setVisibility(View.INVISIBLE);
            }
        }

        final View test = (View) findViewById(R.id.radioGroup1);

//        final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        final Button nextScreen = (Button) findViewById(R.id.nextScreen);
        final Button previousScreen = (Button) findViewById(R.id.backMain);
        previousScreen.setText("Previous");
        previousScreen.setVisibility(View.INVISIBLE);

        nextScreen.setText("Next");

        previousScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuestion--;
                if(currentQuestion == 0){
                    previousScreen.setVisibility(View.INVISIBLE);
                }

                if (currentQuestion < 3) {
                    if (currentQuestion == 2) {
                        nextScreen.setText("Finish Quiz");
                    } else {
                        nextScreen.setText("Next");
                    }

                    radioGroup.check(-1);

                    for(int i = 0; i < 5; i++){
                        checkBox[i].setChecked(false);
                        if (TYPE[currentQuestion] == QuestionType.CHECKBOX) {
                            checkBox[i].setText(ANSWERS[currentQuestion][i]);
                            checkBox[i].setVisibility(View.VISIBLE);
                            radio[i].setVisibility(View.INVISIBLE);

                        } else {
                            radio[i].setText(ANSWERS[currentQuestion][i]);
                            radio[i].setVisibility(View.VISIBLE);
                            checkBox[i].setVisibility(View.INVISIBLE);
                        }
                    }

                    int printCQ = currentQuestion + 1;
                    questionHeaderText.setText("QUESTION " + printCQ + " UP TO " + questionNumber + ":");

                    progressBar.setProgress(currentQuestion);

                    questionText.setText(QUESTIONS[currentQuestion]);

                    for (int i = 0; i < 5; i++) {
                        if(TYPE[currentQuestion] == QuestionType.RADIOBUTTON) {
                            radio[i].setText(ANSWERS[currentQuestion][i]);
                        }
                        else {
                            checkBox[i].setText(ANSWERS[currentQuestion][i]);
                        }
                    }
                }
            }
        });

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getCheckedRadioButtonId() != -1 || checkBox[0].isChecked() ||
                        checkBox[1].isChecked() || checkBox[2].isChecked() ||
                        checkBox[3].isChecked() || checkBox[4].isChecked())
                {
                    currentQuestion++;
                    previousScreen.setVisibility(View.VISIBLE);

                    if (currentQuestion < 3) {
                        if (currentQuestion == 2) {
                            nextScreen.setText("Finish Quiz");
                        } else {
                            nextScreen.setText("Next");
                        }

                        radioGroup.check(-1);

                        for(int i = 0; i < 5; i++){
                            checkBox[i].setChecked(false);
                            if (TYPE[currentQuestion] == QuestionType.CHECKBOX) {
                                checkBox[i].setText(ANSWERS[currentQuestion][i]);
                                checkBox[i].setVisibility(View.VISIBLE);
                                radio[i].setVisibility(View.INVISIBLE);

                            } else {
                                radio[i].setText(ANSWERS[currentQuestion][i]);
                                radio[i].setVisibility(View.VISIBLE);
                                checkBox[i].setVisibility(View.INVISIBLE);
                            }
                        }

                        int printCQ = currentQuestion + 1;
                        questionHeaderText.setText("QUESTION " + printCQ + " UP TO " + questionNumber + ":");

                        progressBar.setProgress(currentQuestion);

                        questionText.setText(QUESTIONS[currentQuestion]);

                        for (int i = 0; i < 5; i++) {
                            if(TYPE[currentQuestion] == QuestionType.RADIOBUTTON) {
                                radio[i].setText(ANSWERS[currentQuestion][i]);
                            }
                            else {
                                checkBox[i].setText(ANSWERS[currentQuestion][i]);
                            }
                        }
                    } else {
                        setResult(1);
                        finish();
                    }
                }
                else{
                    if(TYPE[currentQuestion] == QuestionType.CHECKBOX){
//                        toast.setText("You must choose at least one option for this question");
                        Snackbar.make(test, "You must choose at least one option for this question",
                                Snackbar.LENGTH_SHORT).show();
                    }
                    else{
//                        toast.setText("You must choose an option for this question");
                        Snackbar.make(test, "You must choose an option for this question",
                                Snackbar.LENGTH_SHORT).show();
                    }

//                    toast.show();
                }
            }
        });
    }



    private int currentQuestion = 0;
    private int questionNumber = 3;

    private final QuestionType [] TYPE = {
        QuestionType.CHECKBOX,
        QuestionType.RADIOBUTTON,
        QuestionType.CHECKBOX
    };

    private final String [] QUESTIONS = {
            "PERGUNTA 1",
            "PERGUNTA 2",
            "PERGUNTA 3"
    };

    private final String [][] ANSWERS = {
            {
                    "P1 - R1",
                    "P1 - R2",
                    "P1 - R3",
                    "P1 - R4",
                    "P1 - R5"},
            {
                    "P2 - R1",
                    "P2 - R2",
                    "P2 - R3",
                    "P2 - R4",
                    "P2 - R5"},
            {
                    "P3 - R1",
                    "P3 - R2",
                    "P3 - R3",
                    "P3 - R4",
                    "P3 - R5"},
    };

    TextView questionText;
    RadioButton[] radio = new RadioButton[5];
    CheckBox[] checkBox = new CheckBox[5];
}
