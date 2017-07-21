package com.xfinity.resourceprovider.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements MainView {
  private TextView formatedTextView;
  private TextView dateTextView;
  private TextView pluralsView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    formatedTextView = (TextView) findViewById(R.id.formatted_text_view);
    dateTextView = (TextView) findViewById(R.id.date_string);
    pluralsView = (TextView) findViewById(R.id.plurals_string);

    MainPresenter presenter = new MainPresenter(new ResourceProvider(getApplicationContext()));
    presenter.setView(this);
    presenter.present();
  }

  @Override
  public void setFormattedText(String formattedText) {
    formatedTextView.setText(formattedText);
  }

  @Override
  public void setDateString(String dateString) {
    dateTextView.setText(dateString);
  }

  @Override
  public void setPluralsString(String pluralsString) {
    pluralsView.setText(pluralsString);
  }
}