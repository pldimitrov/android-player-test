/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.examples.youtubeapidemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.AudioManager;
import com.google.android.youtube.player.YouTubeInitializationResult;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.List;

/**
 * A simple YouTube Android API demo application which shows how to use a
 * {@link com.google.android.youtube.player.YouTubeStandalonePlayer} intent to start a YouTube video playback.
 */
public class PausePlayerTest extends Activity implements View.OnClickListener {

  private static final int REQ_START_PAUSE_PLAYER = 1;
  private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

  private static final String VIDEO_ID = "cdgQpa1pUUE";

  private Button playYTButton;

  private Button playMp3Button;
  private Button playVimeoButton;

  private EditText mediaURL;
  private EditText mp3URL;
  private EditText vimeoURL;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//      make our own
    setContentView(R.layout.pause_player_test);

    playYTButton = (Button) findViewById(R.id.play_yt_button);
    playMp3Button = (Button) findViewById(R.id.play_mp3_button);
    playVimeoButton = (Button) findViewById(R.id.play_vimeo_button);
    mediaURL = (EditText) findViewById(R.id.yt_url);
    mp3URL = (EditText) findViewById(R.id.mp3_url);
    vimeoURL = (EditText) findViewById(R.id.vimeo_url);

    playYTButton.setOnClickListener(this);
    playMp3Button.setOnClickListener(this);
    playVimeoButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
      System.out.println("do I reach here");

    final String URL = mediaURL.getText().toString();

    Intent intent = null;
    if (v == playYTButton) {
      intent = YouTubeStandalonePlayer.createVideoIntent(
          this, DeveloperKey.DEVELOPER_KEY, URL);
    }

    if (v == playMp3Button) {


        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            System.out.println("do I reach here");
            mediaPlayer.setDataSource(mp3URL.getText().toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
//            intent = mediaPlayer;
        }
        catch (java.io.IOException e) {

        }

    }

    if (v == playVimeoButton) {
    }

    if (intent != null) {
      if (canResolveIntent(intent)) {
        startActivityForResult(intent, REQ_START_PAUSE_PLAYER);
      } else {
        // Could not resolve the intent - must need to install or update the YouTube API service.
        YouTubeInitializationResult.SERVICE_MISSING
            .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQ_START_PAUSE_PLAYER && resultCode != RESULT_OK) {
      YouTubeInitializationResult errorReason =
          YouTubeStandalonePlayer.getReturnedInitializationResult(data);
      if (errorReason.isUserRecoverableError()) {
        errorReason.getErrorDialog(this, 0).show();
      } else {
        String errorMessage =
            String.format(getString(R.string.error_player), errorReason.toString());
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
      }
    }
  }

  private boolean canResolveIntent(Intent intent) {
    List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
    return resolveInfo != null && !resolveInfo.isEmpty();
  }

  private int parseInt(String text, int defaultValue) {
    if (!TextUtils.isEmpty(text)) {
      try {
        return Integer.parseInt(text);
      } catch (NumberFormatException e) {
        // fall through
      }
    }
    return defaultValue;
  }

}
