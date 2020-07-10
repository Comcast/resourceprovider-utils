package com.xfinity.resourceprovider.sample_library;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainView {
  private TextView formattedTextView;
  private TextView dateTextView;
  private TextView pluralsView;
  private TextView dimenView;
  private TextView integerView;
  private TextView idView;
  private ImageView imageView;
  private View colorView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    formattedTextView = findViewById(R.id.formatted_text_view);
    dateTextView = findViewById(R.id.date_string);
    pluralsView = findViewById(R.id.plurals_string);
    imageView = findViewById(R.id.image);
    dimenView = findViewById(R.id.dimen_text);
    integerView = findViewById(R.id.integer_text);
    idView = findViewById(R.id.id_text);
    colorView = findViewById(R.id.color_view);

    MainPresenter presenter = new MainPresenter(new ResourceProvider(getApplicationContext()));
    presenter.setView(this);
    presenter.present();
  }

  @Override
  public void setFormattedText(String formattedText) {
    formattedTextView.setText(formattedText);
  }

  @Override
  public void setDateString(String dateString) {
    dateTextView.setText(dateString);
  }

  @Override
  public void setPluralsString(String pluralsString) {
    pluralsView.setText(pluralsString);
  }

  @Override
  public void setDrawable(Drawable drawable) {
    imageView.setImageDrawable(drawable);
  }

  @Override
  public void setDimenText(String dimenText) {
    dimenView.setText(dimenText);
  }

  @Override
  public void setIntegerText(String integerText) {
    integerView.setText(integerText);
  }

  @Override
  public void setIdText(String idText) {
    idView.setText(idText);
  }

  @Override
  public void setColorViewBackgroundColor(int color) {
    colorView.setBackgroundColor(color);
  }
}