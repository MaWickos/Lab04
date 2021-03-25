package pollub.ism.lab04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // MAKRA I PREDEFINICJE
    private final int KOLKO = 0;
    private final int KRZYZYK = 1;
    private final int PUSTE = -1;

    // Zmienna do przechowywania aktualnego ruchu
    private int KolkoCzyKrzyzyk = KOLKO;
    private int liczbaRuchow = 0;

    // Plansza logiczna do zapisywania występującego znaku na polu
    private int[][] plansza = new int[3][3];

    // Zapisanie obiektów buttonów
    private Button[] przyciski = new Button[9];

    // Zmienne do przechowywania wartości
    private static  String  KEY_PLANSZA_W0 = "Wartosci pol na planszy logicznej w wierszu W1",
                            KEY_PLANSZA_W1 = "Wartosci pol na planszy logicznej w wierszu W2",
                            KEY_PLANSZA_W2 = "Wartosci pol na planszy logicznej w wierszu W3",
                            KEY_CZYJ_RUCH = "Aktualny ruch",
                            KEY_ILE_RUCHOW = "Liczba wykonanych ruchow";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja planszy - przypisanie wartości "NULL" - czyli (-1)
        int temp = 0;
        for(int w = 0; w < 3; w++){
           for(int k = 0; k < 3; k++) {
               plansza[w][k] = PUSTE;
               przyciski[temp++] = findViewById(getResources().getIdentifier("button_" + w + "_" + k, "id", getPackageName()));
           }
        }

    }

    // Funkcja do wyświetlania komnikatu o wygranej
    private void wyswietlKomunikatWygranej(int znak){
        switch (znak) {
            case KOLKO:
                Toast.makeText(this, "Wygralo kolko \"O\"", Toast.LENGTH_LONG).show();
                break;

            case KRZYZYK:
                Toast.makeText(this, "Wygral krzyzyk \"X\"", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /* Funkcja do sprawdzenia wygranej
    * TRUE - brak wygranej, mozna kontynuowac rozgrywke
    * FALSE - wygrana ktoregos z graczy
    * */
    private boolean czyMoznaKontynuowacRozgrywke(){

        int pierwszyZnak;
        boolean czyZnakiSaIdentyczne = false;

        // Sprawdzanie wierszy
        for(int w = 0; w < 3; w++){
            pierwszyZnak = plansza[w][0];

            // Przechodzenie po kolumnach
            for(int k =0; k<3; k++){

                if(pierwszyZnak == plansza[w][k]){
                    czyZnakiSaIdentyczne = true;        // Znaki identyczne, sprawdzamy dalej
                } else {
                    czyZnakiSaIdentyczne = false;       // Znak nie zgadza sie z pierwszym - zakoncz dalsze sprawdzanie wiersza
                    break;
                }
            }

            // Wygrana w wierszu
            if(czyZnakiSaIdentyczne && pierwszyZnak != -1){
                wyswietlKomunikatWygranej(plansza[w][0]);       // Komunikat o wygranej
                return false;                                   // Nie można kontynuować rozgrywki
            }
        }

        // Sprawdzenie kolumn
        for(int k = 0; k < 3; k++){

            pierwszyZnak = plansza[0][k];

            // Przechodzenie po wierszach
            for(int w = 0; w < 3; w++){

                if(pierwszyZnak == plansza[w][k]){
                    czyZnakiSaIdentyczne = true;        // Znaki w kolumnie są identyczne, kontynuuj sprawdzanie
                } else {
                    czyZnakiSaIdentyczne = false;       // Znaki nie są identyczne, przerwij sprawdzanie
                    break;
                }
            }

            // Wygrana w kolumnie
            if(czyZnakiSaIdentyczne && pierwszyZnak != -1){
                wyswietlKomunikatWygranej(plansza[0][k]);   // Komunikat o wygranej
                return false;                               // Nie można kontynuować rozgrywki
            }
        }

        // Sprawdzenie przekatnych
        if(plansza[0][0] == plansza[1][1] && plansza[1][1] == plansza[2][2] && plansza[1][1] != -1){
            wyswietlKomunikatWygranej(plansza[1][1]);       // Komunikat o wygranej
            return false;                                   // Nie można kontynuować rozgrywki

        } else if (plansza[0][2] == plansza[1][1] && plansza[1][1] == plansza[2][0] && plansza[1][1] != -1){
            wyswietlKomunikatWygranej(plansza[1][1]);       // Komunikat o wygranej
            return false;                                   // Nie można kontynuować rozgrywki
        }

        // Brak wygranych - kontynuowanie gry
        return true;
    }

    /* Naciśnięcie przycisku powoduje postawienie "X" i "O" */
    public void kliknieciePrzycisku(View view){

        // Pobranie nazwy przycisku, który wywołal metodę
        String nazwaPrzycisku = view.getResources().getResourceEntryName(view.getId());

        // Rozbicie łańcucha na 3 elementy (button + nrWiersza + nrKolumny)
        String rozbite_id[] = nazwaPrzycisku.split("_");
        int nrWiersza = Integer.parseInt(rozbite_id[1]);
        int nrKolumny = Integer.parseInt(rozbite_id[2]);

        // Obiekt klasy button
        Button button = (Button)findViewById(view.getId());

        // Czyj ruch?
        switch (KolkoCzyKrzyzyk){

            case KOLKO:
                button.setText("O");                        // Rysowanie znaku gracza
                KolkoCzyKrzyzyk = KRZYZYK;                  // Zmiana ruchu
                plansza[nrWiersza][nrKolumny] = KOLKO;      // Zapis znaku do planszy
                break;

            case KRZYZYK:
                button.setText("X");
                KolkoCzyKrzyzyk = KOLKO;
                plansza[nrWiersza][nrKolumny] = KRZYZYK;
                break;

        }

        button.setEnabled(false);   // Wyłączenie możliwości kliknięcia przycisku ponownie
        liczbaRuchow++;             // Inkrementacja liczby ruchów

        // Sprawdzenie wygranej
        if(liczbaRuchow >=5){

        // WYGRANA
            if(!czyMoznaKontynuowacRozgrywke()){

                // Blokowanie przyciskow
                for(int i=0; i<9; i++){
                    przyciski[i].setEnabled(false);
                }

        // REMIS
            } else if(liczbaRuchow == 9){
                Toast.makeText(this, "Remis! Powodzenia nastepnym razem :)", Toast.LENGTH_LONG).show();
            }

        // GRA TOCZY SIE DALEJ

        }

    }

    // Czyszczenie planszy i zmiennych
    public void zacznijOdNowa(View view){

        // Zerowanie zmiennych
        liczbaRuchow = 0;
        KolkoCzyKrzyzyk = KOLKO;

        // Aktywacja buttonów, czyszczenie etykiet
        int temp = 0;
        for(int w = 0; w < 3; w++){
            for(int k = 0; k < 3; k++) {
                plansza[w][k] = PUSTE;
                przyciski[temp].setEnabled(true);
                przyciski[temp].setText("");
                temp++;
            }
        }
    }

    // Zapisywanie i odtwarzanie wartości pól klasy aktywności
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Zapis zmiennych
        outState.putIntArray(KEY_PLANSZA_W0, plansza[0]);
        outState.putIntArray(KEY_PLANSZA_W1, plansza[1]);
        outState.putIntArray(KEY_PLANSZA_W2, plansza[2]);
        outState.putInt(KEY_CZYJ_RUCH, KolkoCzyKrzyzyk);
        outState.putInt(KEY_ILE_RUCHOW, liczbaRuchow);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        // Przywrócenie wartości zmiennych
        plansza[0] = savedInstanceState.getIntArray(KEY_PLANSZA_W0);
        plansza[1] = savedInstanceState.getIntArray(KEY_PLANSZA_W1);
        plansza[2] = savedInstanceState.getIntArray(KEY_PLANSZA_W2);

        KolkoCzyKrzyzyk = savedInstanceState.getInt(KEY_CZYJ_RUCH, 0) ;
        liczbaRuchow = savedInstanceState.getInt(KEY_ILE_RUCHOW, 0);

        // Zaznaczenie pol juz wybranych (Zablokowanie buttonow, wpisanie odpowiednich znakow)
        int temp = 0;
        for(int w=0; w<3; w++){
            for(int k=0; k<3; k++){
                if(plansza[w][k] == KOLKO){
                    przyciski[temp].setText("O");
                    przyciski[temp].setEnabled(false);
                } else if(plansza[w][k] == KRZYZYK){
                    przyciski[temp].setText("X");
                    przyciski[temp].setEnabled(false);
                }
                temp++;
            }
        }

        // Nie można kontynuować - zablokuj przyciski
        if(!czyMoznaKontynuowacRozgrywke()){
            for(int i=0; i<9; i++){
                przyciski[i].setEnabled(false);
            }
        }
    }
}