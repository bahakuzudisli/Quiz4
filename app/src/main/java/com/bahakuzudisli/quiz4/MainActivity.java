package com.bahakuzudisli.quiz4;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView usaFlag;
    ImageView euroFlag;
    ImageView ukFlag;
    ImageView japanFlag;
    EditText editTextMiktarGir1;
    EditText editTextMiktarGir2;
    TextView textAmount;
    TextView textTitle;
    ListView listView;
    static String currentCurrency = "USD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        startTitleAnim();

        editTextMiktarGir1.addTextChangedListener(textWatcher1);
        editTextMiktarGir2.addTextChangedListener(textWatcher2);

        // Verileri hazırla
        String[] currencies = {"USD", "EUR", "GBP", "YEN"};
        int[] images = {R.drawable.usa, R.drawable.eu, R.drawable.uk,R.drawable.japan};

        // Özel ArrayAdapter oluştur
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, currencies,images);

        // ListView'e adapter'i ayarla
        listView.setAdapter(adapter);

        initListViewClickListener();

    }

    private void startTitleAnim() {
        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(
                textTitle,
                "textColor",
                new ArgbEvaluator(),
                0xFFFA58F4,  // PINK
                0xFFFFFF00   // YELLOW
        );

        colorAnimator.setDuration(1000); // Animasyon süresi (1 saniye)
        colorAnimator.setRepeatCount(ObjectAnimator.INFINITE); // Sonsuz tekrar
        colorAnimator.setRepeatMode(ObjectAnimator.REVERSE); // Tersine döndürme

        // Animasyonu başlat
        colorAnimator.start();
    }

    private void initListViewClickListener() {
        // ListView öğelerine tıklama dinleyicisi ekle
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan öğenin verisini al
                String selectedCurrency = (String) parent.getItemAtPosition(position);

                if(selectedCurrency.matches("USD")) {
                    currentCurrency = "USD";
                    textAmount.setText("Amount(USD)");
                } else if (selectedCurrency.matches("EUR")) {
                    currentCurrency = "EUR";
                    textAmount.setText("Amount(EUR)");
                }else if (selectedCurrency.matches("GBP")) {
                    currentCurrency = "GBP";
                    textAmount.setText("Amount(GBP)");
                }else if (selectedCurrency.matches("YEN")) {
                    currentCurrency = "YEN";
                    textAmount.setText("Amount(YEN)");
                } else {
                    currentCurrency = "USD";
                    textAmount.setText("Amount(USD)");
                }

                // Toast mesajı ile göster
                Toast.makeText(getApplicationContext(), "Selected Currency: " + selectedCurrency, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CustomArrayAdapter extends ArrayAdapter<String> {

        private int[] images;

        public CustomArrayAdapter(Context context, String[] currencies, int[] images) {
            super(context, R.layout.custom_list_item, currencies);
            this.images = images;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Liste öğesinin görünümünü al
            View listItemView = convertView;
            if (listItemView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                listItemView = inflater.inflate(R.layout.custom_list_item, parent, false);
            }

            // Öğeleri bul
            ImageView imageView = listItemView.findViewById(R.id.customImageView);
            TextView textView = listItemView.findViewById(R.id.customTextView);

            // Veriyi al
            String currency = getItem(position);
            int imageResource = images[position];

            // Görünümü güncelle
            textView.setText(currency);
            imageView.setImageResource(imageResource);

            return listItemView;
        }
    }


    private TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                editTextMiktarGir2.removeTextChangedListener(textWatcher2);
                if (!charSequence.toString().isEmpty()) {
                    double convertedValue = convertCurrency(Double.parseDouble(charSequence.toString()));
                    editTextMiktarGir2.setText(String.valueOf(convertedValue));
                } else {
                    editTextMiktarGir2.setText("");
                }
                editTextMiktarGir2.addTextChangedListener(textWatcher2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                editTextMiktarGir1.removeTextChangedListener(textWatcher1);
                if (!charSequence.toString().isEmpty()) {
                    double convertedValue = convertTLToCurrency(Double.parseDouble(charSequence.toString()));
                    editTextMiktarGir1.setText(String.valueOf(convertedValue));
                } else {
                    editTextMiktarGir1.setText("");
                }
                editTextMiktarGir1.addTextChangedListener(textWatcher1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void initializeViews() {
        listView = findViewById(R.id.listView);
        textTitle = findViewById(R.id.textTitle);
        editTextMiktarGir1 = findViewById(R.id.editTextMiktarGir1);
        editTextMiktarGir2 = findViewById(R.id.editTextMiktarGir2);
        textAmount = findViewById(R.id.textAmount);
    }

    public static double convertCurrency(double amount) {
        try {
            switch (currentCurrency) {
                case "USD":
                    return amount / 18.5;
                case "EUR":
                    return amount / 18.3;
                case "GBP":
                    return amount / 21.1;
                case "YEN":
                    return amount / 7.84;
                default:
                    System.out.println("Invalid currency");
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double convertTLToCurrency(double amount) {
        try {
            switch (currentCurrency) {
                case "USD":
                    return amount * 18.5;
                case "EUR":
                    return amount * 18.3;
                case "GBP":
                    return amount * 21.1;
                case "YEN":
                    return amount * 7.84;
                default:
                    System.out.println("Invalid currency");
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
