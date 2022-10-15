package com.example.funquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.funquiz.Model.Questions;
import com.example.funquiz.Model.Score;
import com.example.funquiz.Utils.Prefs;
import com.example.funquiz.Utils.Repository;
import com.example.funquiz.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private ActivityMainBinding binding;
   private List<Questions>questionList;
   private int questionIndex = 0;
   private int max=4;

   private int min = 1;
   private int randomNumber;
   private static int score = 0;
   private boolean isCorrect=true;
   private AlertDialog.Builder correctBox;
   private AlertDialog.Builder wrongBox;
   private SoundPool soundPool;
   private AudioAttributes audioAttributes;
   private int happyNoise,weirdNoise;
   private LottieAnimationView lottieAnimationView;
   Score currScore ;

   Prefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //setting the requirements.
        currScore = new Score();
        binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);

        prefs = new Prefs(MainActivity.this);
        binding.highScoreTextView.setText(MessageFormat.format("High-Score:{0}", prefs.getHighestScore()));

        audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .build();

        soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();

        Snackbar.make(binding.questionCardView,"Internet required",Snackbar.LENGTH_SHORT)
                .show();

        happyNoise = soundPool.load(MainActivity.this,R.raw.complete,1);
        weirdNoise = soundPool.load(MainActivity.this,R.raw.fun_laugh,1);

        questionList = new Repository().getQ_List(questionListResponse -> {
            binding.qCardTextView.setText((questionIndex+1)+" ."+questionListResponse.get(0).getQuestion());
           randomNumber = (int)(Math.random()*(max-min+1)+min);

            switch(randomNumber){
                case 1:
                    binding.option1CardTextView.setText(questionListResponse.get(questionIndex).getRightAns());
                    binding.option2CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns1());
                    binding.option3CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns2());
                    binding.option4CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns3());
                    Log.d("check", "onClick: clicked "+randomNumber);

                    break;
                case 2:
                    binding.option1CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns1());
                    binding.option2CardTextView.setText(questionListResponse.get(questionIndex).getRightAns());
                    binding.option3CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns2());
                    binding.option4CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns3());
                    Log.d("check", "onClick: clicked "+randomNumber);
                    break;
                case 3:
                    binding.option1CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns3());
                    binding.option2CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns1());
                    binding.option3CardTextView.setText(questionListResponse.get(questionIndex).getRightAns());
                    binding.option4CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns2());
                    Log.d("check", "onClick: clicked "+randomNumber);
                    break;

                case 4:
                    binding.option1CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns1());
                    binding.option2CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns3());
                    binding.option3CardTextView.setText(questionListResponse.get(questionIndex).getWrongAns2());
                    binding.option4CardTextView.setText(questionListResponse.get(questionIndex).getRightAns());
                    Log.d("check", "onClick: clicked "+randomNumber);
                    break;
            }


        });



        binding.option1CardTextView.setOnClickListener(view -> {

            if(binding.option1CardTextView.getText().toString().equals(questionList.get(questionIndex).getRightAns())){
                addPoints();

            }else{
                deductPoints();
                Toast.makeText(MainActivity.this,"Wrong ans",Toast.LENGTH_SHORT)
                        .show();

            }
            updateQuestion();
        });

        binding.option2CardTextView.setOnClickListener(view->{
            if(binding.option2CardTextView.getText().toString().equals(questionList.get(questionIndex).getRightAns())){

                addPoints();

            }else{
                deductPoints();
                Toast.makeText(MainActivity.this,"Wrong ans",Toast.LENGTH_SHORT)
                        .show();

            }
            updateQuestion();
        });

        binding.option3CardTextView.setOnClickListener(view -> {
            if(binding.option3CardTextView.getText().toString().equals(questionList.get(questionIndex).getRightAns())){
                addPoints();

            }else{
                deductPoints();
                Toast.makeText(MainActivity.this,"Wrong ans",Toast.LENGTH_SHORT)
                        .show();

            }
            updateQuestion();
        });

        binding.option4CardTextView.setOnClickListener(view->{
            if(binding.option4CardTextView.getText().toString().equals(questionList.get(questionIndex).getRightAns())){
                addPoints();

            }else{
                deductPoints();
                Toast.makeText(MainActivity.this,"Wrong ans",Toast.LENGTH_SHORT)
                        .show();

            }
            updateQuestion();
        });


        //next btn
        binding.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion();
            }
        });

        //prev btn
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionIndex>0)
                {
                    backTrackQuestion();
                }
                else {
                    Toast.makeText(MainActivity.this, "You are in first Question !", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private void checkAnswer(){
        if(isCorrect){
            fadeAnimation();
        }else{
            shakeAnimation();
        }
    }

    private void deductPoints(){

        if(score>0){
            isCorrect = false;
            score-=10;
            currScore.setScore(score);
            binding.scoreTextView.setText("SCORE :"+score);
            wrongAnswerDialogBox();
            wrongBox.show();
        }
        soundPool.play(weirdNoise,1,1,0,0,1);
    }

    private void addPoints(){
        isCorrect = true;
        score+=20;

        currScore.setScore(score);
            correctAnswerDialogBox();
            correctBox.show();
            soundPool.play(happyNoise,1,1,0,0,1);
        binding.scoreTextView.setText("SCORE :"+score);
    }



    //this method plays the winner anim when the curr score crosses the high score
    /*private void playWinnerAnimation() {
        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.setContentView(R.layout.winner_animation);
        dialog.show();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }*/


    private void shakeAnimation(){
        Animation animationShake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);

        binding.questionCardView.setAnimation(animationShake);
//        binding.scoreTextView.setAnimation(animationShake);

        animationShake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.qCardTextView.setTextColor(Color.RED);
//                binding.scoreTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.qCardTextView.setTextColor(Color.WHITE);
//                binding.scoreTextView.setTextColor(Color.argb(1,255,112,67));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(700);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(1);

        binding.questionCardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.qCardTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.qCardTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void backTrackQuestion() {
            questionIndex = (questionIndex - 1) % questionList.size();
            update_QuestionCard();
    }



    private void updateQuestion() {
        questionIndex = (questionIndex+1)%questionList.size();
        fadeAnimation();
        update_QuestionCard();

        //shakeAnimation();
    }

    private void update_QuestionCard() {
        String next_Q = questionList.get(questionIndex).getQuestion();
        binding.qCardTextView.setText((questionIndex+1)+" ."+next_Q.trim());

        randomNumber = (int)(Math.random()*(max-min+1)+min);

        switch (randomNumber){
            case 1 :
                binding.option1CardTextView.setText(questionList.get(questionIndex).getWrongAns1());
                binding.option2CardTextView.setText(questionList.get(questionIndex).getRightAns());
                binding.option3CardTextView.setText(questionList.get(questionIndex).getWrongAns2());
                binding.option4CardTextView.setText(questionList.get(questionIndex).getWrongAns3());
                break;

            case 2:
                binding.option1CardTextView.setText(questionList.get(questionIndex).getRightAns());
                binding.option2CardTextView.setText(questionList.get(questionIndex).getWrongAns1());
                binding.option3CardTextView.setText(questionList.get(questionIndex).getWrongAns2());
                binding.option4CardTextView.setText(questionList.get(questionIndex).getWrongAns3());
                break;

            case 3:
                binding.option1CardTextView.setText(questionList.get(questionIndex).getWrongAns1());
                binding.option2CardTextView.setText(questionList.get(questionIndex).getWrongAns3());
                binding.option3CardTextView.setText(questionList.get(questionIndex).getWrongAns2());
                binding.option4CardTextView.setText(questionList.get(questionIndex).getRightAns());
                break;

            case 4:
                binding.option1CardTextView.setText(questionList.get(questionIndex).getWrongAns1());
                binding.option2CardTextView.setText(questionList.get(questionIndex).getWrongAns2());
                binding.option3CardTextView.setText(questionList.get(questionIndex).getRightAns());
                binding.option4CardTextView.setText(questionList.get(questionIndex).getWrongAns3());
                break;
        }

    }

    //dialog box 1
    private void  correctAnswerDialogBox(){
        correctBox = new AlertDialog.Builder(MainActivity.this);
        correctBox.setIcon(R.drawable.correct_symbol);
        correctBox.setTitle("CONGO! its Right Answer.");
        correctBox.setMessage("Your score is : "+score);



        //cancel button
        correctBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("check", "onClick: cancel");

            }
        });
    }

    //dialog box 2
    private void wrongAnswerDialogBox(){
        wrongBox = new AlertDialog.Builder(MainActivity.this);
        wrongBox.setIcon(R.drawable.incorrect_symbol);
        wrongBox.setTitle("Incorrect Answer !");
        wrongBox.setMessage("Your score is : "+score);


        wrongBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    @Override
    protected void onPause() {
        prefs.setHighestScore(currScore.getScore());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(soundPool!=null){
            soundPool.release();
            soundPool = null;
        }

        lottieAnimationView.cancelAnimation();
    }
}