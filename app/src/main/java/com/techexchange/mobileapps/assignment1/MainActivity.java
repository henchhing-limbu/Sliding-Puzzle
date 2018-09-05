package com.techexchange.mobileapps.assignment1;

import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<NumberImages> numbersList;
    private ImageView[] numberImages = new ImageView[9];
    private String[] imageNames = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight"};
    private HashMap<String, Integer> imageNamesToIndex = new HashMap<>();
    private HashMap<Integer, Integer> indexToResID = new HashMap<>();
    private HashMap<Integer, Integer> resIdToIndex = new HashMap<>();
    private int emptySpaceIndex = -1;
    private int slidingImageIndex = -1;
    private int emptySpaceTag = 1;

    private ImageView img0;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;
    private ImageView img8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numbersList = initNumbersList();

        img0 = findViewById(R.id.zero);
        img1 = findViewById(R.id.one);
        img2 = findViewById(R.id.two);
        img3 = findViewById(R.id.three);
        img4 = findViewById(R.id.four);
        img5 = findViewById(R.id.five);
        img6 = findViewById(R.id.six);
        img7 = findViewById(R.id.seven);
        img8 = findViewById(R.id.eight);

        numberImages[0] = img0;
        numberImages[1] = img1;
        numberImages[2] = img2;
        numberImages[3] = img3;
        numberImages[4] = img4;
        numberImages[5] = img5;
        numberImages[6] = img6;
        numberImages[7] = img7;
        numberImages[8] = img8;

        initImageNamesToIndex();

        for (int j = 0; j < 9; j++) {
            numberImages[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageViewPressed(v);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestryo() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        updateView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    private List<NumberImages> initNumbersList() {
        List<NumberImages> numList = new ArrayList<>();
        int length = 9;
        numList.add(new NumberImages(0, 0, 1));
        for (int i = 1; i < length; i++) {
            numList.add(new NumberImages(i, 0, 0));
        }
        return numList;
    }

    private void updateView() {
        Log.d(TAG, "updateView() called");
        if (emptySpaceIndex  < 0 || slidingImageIndex < 0) {
            emptySpaceIndex = 0;
            shuffleNumbers();
            for (int i = 0; i < numbersList.size(); i++) {
                NumberImages num = numbersList.get(i);
                if (i != emptySpaceIndex) {
                    int resId = getResources().getIdentifier(imageNames[i], "drawable", "com.techexchange.mobileapps.assignment1");
                    numberImages[i].setImageResource(resId);
                    numberImages[i].setTag(resId);

                    indexToResID.put(i, resId);
                    resIdToIndex.put(resId, imageNamesToIndex.get(imageNames[i]));
                }
                else {
                    numberImages[i].setImageDrawable(null);
                    numberImages[i].setTag(-1);

                    indexToResID.put(i, emptySpaceTag);
                    resIdToIndex.put(emptySpaceTag, imageNamesToIndex.get(imageNames[i]));
                }
            }
            Log.d(TAG, "Resource id to index " + resIdToIndex.toString());
            Log.d(TAG, "Index to resource id " + indexToResID.toString());
        }
        else {
            // swapping of tiles
            // updating index to resource ID mapping
            numberImages[emptySpaceIndex].setImageDrawable(null);
            numberImages[emptySpaceIndex].setTag(emptySpaceTag);
            indexToResID.put(emptySpaceIndex, emptySpaceTag);

            numberImages[slidingImageIndex].setImageResource(indexToResID.get(slidingImageIndex));
            numberImages[slidingImageIndex].setTag(indexToResID.get(slidingImageIndex));
            indexToResID.put(slidingImageIndex, indexToResID.get(slidingImageIndex));
        }

        // checking for win condition
        if (checkWinCondition()) {
            Log.d(TAG, "Won the game");
            for (int i = 0; i < 9; i++) {
                numberImages[i].setColorFilter(getResources().getColor(R.color.green));
                numberImages[i].setEnabled(false);
                Toast.makeText(MainActivity.this, "Congratulations! You won the game", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onImageViewPressed(View v) {
        ImageView selectedImage = (ImageView) v;
        int currentIndex = 0;
        Integer resId = (Integer) selectedImage.getTag();
        for (Integer x: indexToResID.keySet()) {
            if (indexToResID.get(x).equals(resId)) {
                currentIndex = x;
                break;
            }
        }

        // swapping the position of empty space and the number
        if ((emptySpaceIndex % 3) == 0) {
            if (((currentIndex - 1) == emptySpaceIndex) || ((currentIndex + 3) == emptySpaceIndex) || ((currentIndex - 3) == emptySpaceIndex)) {
                slidingImageIndex = emptySpaceIndex;
                indexToResID.put(slidingImageIndex, resId);
                emptySpaceIndex = currentIndex;
                indexToResID.put(emptySpaceIndex, emptySpaceTag);
                updateView();
            }
        } else if (((emptySpaceIndex - 2) % 3) == 0) {
            if (((currentIndex - 1) == emptySpaceIndex) || ((currentIndex + 1) == emptySpaceIndex) || ((currentIndex + 3) == emptySpaceIndex) || ((currentIndex - 3) == emptySpaceIndex)) {
                slidingImageIndex = emptySpaceIndex;
                indexToResID.put(slidingImageIndex, resId);
                emptySpaceIndex = currentIndex;
                indexToResID.put(emptySpaceIndex, emptySpaceTag);
                updateView();
            }
        } else if (((emptySpaceIndex - 1) % 3) == 0) {
            if (((currentIndex - 1) == emptySpaceIndex) || ((currentIndex + 1) == emptySpaceIndex) || ((currentIndex + 3) == emptySpaceIndex) || ((currentIndex - 3) == emptySpaceIndex)) {
                slidingImageIndex = emptySpaceIndex;
                indexToResID.put(slidingImageIndex, resId);
                emptySpaceIndex = currentIndex;
                indexToResID.put(emptySpaceIndex, emptySpaceTag);
                updateView();
            }
        }
        else {
            updateView();
        }
    }

    // TODO: Fix this to shuffle properly
    private void shuffleNumbers() {
        int moves = 20;
        while (moves != 0) {
            double randomValue = Math.random();
            if (emptySpaceIndex % 3 == 0) {
                if ((emptySpaceIndex - 3) < 0) {
                    if (randomValue < 0.5) {
                        shuffleMove(1, true);
                    }
                    else {
                        shuffleMove(3, true);
                    }
                } else if ((emptySpaceIndex + 3) > 8) {
                    if (randomValue < 0.5) {
                        shuffleMove(1, true);
                    }
                    else {
                        shuffleMove(3, false);
                    }
                } else {
                    if (randomValue < 0.33) {
                        shuffleMove(3, true);
                    }
                    else if (randomValue < 0.66) {
                        shuffleMove(1, true);
                    }
                    else {
                        shuffleMove(3, false);
                    }
                }
            }

            else if ((emptySpaceIndex - 1) % 3 == 0) {
                if ((emptySpaceIndex -3) < 0) {
                    if (randomValue < 0.33) {
                        shuffleMove(1, false);
                    }
                    else if (randomValue < 0.66) {
                        shuffleMove(1, true);
                    }
                    else {
                        shuffleMove(3, true);
                    }
                }
                else if ((emptySpaceIndex + 3 ) > 0) {
                    if (randomValue < 0.33) {
                        shuffleMove(1, false);
                    }
                    else if (randomValue < 0.66) {
                        shuffleMove(1, true);
                    }
                    else {
                        shuffleMove(3, false);
                    }
                }
                else {
                    if (randomValue < 0.25) {
                        shuffleMove(1, false);
                    }
                    else if (randomValue < 0.50) {
                        shuffleMove(1, true);
                    }
                    else if (randomValue < 0.75){
                        shuffleMove(3, true);
                    }
                    else {
                        shuffleMove(3, false);
                    }
                }
            }
            else {
                if ((emptySpaceIndex - 3) < 0) {
                    if (randomValue < 0.5) {
                        shuffleMove(1, false);
                    }
                    else {
                        shuffleMove(3, true);
                    }
                } else if ((emptySpaceIndex + 3) > 8) {
                    if (randomValue < 0.5) {
                        shuffleMove(1, false);
                    }
                    else {
                        shuffleMove(3, false);
                    }
                } else {
                    if (randomValue < 0.33) {
                        shuffleMove(3, false);
                    }
                    else if (randomValue < 0.66) {
                        shuffleMove(1, false);
                    }
                    else {
                        shuffleMove(3, true);
                    }
                }
            }
            moves -= 1;
        }
        Log.d(TAG, "imageNames are " + Arrays.deepToString(imageNames));
        Log.d(TAG, "empty space index is " + emptySpaceIndex);
    }

    private void shuffleMove(int i, boolean operate) {
        if (operate) {
            String temp = imageNames[emptySpaceIndex];
            imageNames[emptySpaceIndex] = imageNames[emptySpaceIndex + i];
            imageNames[emptySpaceIndex + i] = temp;
            emptySpaceIndex = emptySpaceIndex + i;
        }
        else {
            String temp = imageNames[emptySpaceIndex];
            imageNames[emptySpaceIndex] = imageNames[emptySpaceIndex - i];
            imageNames[emptySpaceIndex - i] = temp;
            emptySpaceIndex = emptySpaceIndex - i;
        }
    }

    private boolean checkWinCondition() {
        for (Integer index: indexToResID.keySet()) {
            Integer resId = indexToResID.get(index);
            if (resIdToIndex.get(resId) != index)
                return false;
        }
        return true;
    }

    private void initImageNamesToIndex() {
        int length = 9;
        for (int i = 0; i < length; i++) {
            imageNamesToIndex.put(imageNames[i], i);
        }
    }
}

