package com.example.lyrio.modules.musica.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.lyrio.R;
import com.example.lyrio.modules.musica.viewmodel.LetrasViewModel;
import com.example.lyrio.service.api.VagalumeBuscaApi;
import com.example.lyrio.database.models.Musica;
import com.example.lyrio.util.Constantes;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TelaLetrasActivity extends AppCompatActivity {

    private TextView nomeDoArtista;
    private TextView nomeDaMusica;
    private TextView letraDaMusica;
    private CircleImageView imagemArtista;
    private Retrofit retrofit;
    private ToggleButton favourite_button;
    private LetrasViewModel letrasViewModel;


    //Associar ao termo "VAGALUME" para filtrar no LOGCAT
    private static final String TAG = "VAGALUME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_letras);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String musicaSalvaId =  bundle.getString("MUSICA_ID");

        letrasViewModel = ViewModelProviders.of(this).get(LetrasViewModel.class);

        // Iniciar retrofit para buscar infos da API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.vagalume.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nomeDaMusica = findViewById(R.id.letras_nome_musica_text_view);
        nomeDoArtista = findViewById(R.id.letras_nome_artista_text_view);
        letraDaMusica = findViewById(R.id.letras_letra_musica_text_view);
        imagemArtista = findViewById(R.id.letras_artist_pic);
        favourite_button = findViewById(R.id.letras_favorito_button);

        letrasViewModel.getMusicaPorId(musicaSalvaId);

        getApiData(musicaSalvaId);

        letrasViewModel.getMusicaLiveData()
                .observe(this, musica -> {
                            favourite_button.setSelected(musica != null);
                        });

                    favourite_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (favourite_button.isChecked()) {
                                Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_ADICIONAR, Toast.LENGTH_SHORT).show();
                                Musica musica = new Musica();
                                musica.setId(musicaSalvaId);
                                letrasViewModel.favoritarMusica(musica);

                            } else {
                                Toast.makeText(TelaLetrasActivity.this, Constantes.TOAST_MUSICA_FAVORITA_EXCLUIR, Toast.LENGTH_SHORT).show();

                                letrasViewModel.removerMusicaPorId(musicaSalvaId);
                            }
                        }
                    });

    }

    private void getApiData(String idDaMusica) {

        Date curTime = Calendar.getInstance().getTime();

        idDaMusica = idDaMusica.trim().replace(" ", "-");
        String vagaKey =  Constantes.VAGALUME_KEY + curTime.toString().trim().replace(" ","");
        String buscaFull = "https://api.vagalume.com.br/search.php?apikey="+vagaKey+"&musid="+idDaMusica;

        VagalumeBuscaApi service = retrofit.create(VagalumeBuscaApi.class);
//        Call<VagalumeBusca> vagalumeBuscaCall = service.getBuscaResponse(buscaFull);
//        vagalumeBuscaCall.enqueue(new Callback<VagalumeBusca>() {
//            @Override
//            public void onResponse(Call<VagalumeBusca> call, Response<VagalumeBusca> response) {
//                if(response.isSuccessful()){
//                    VagalumeBusca vagalumeBusca = response.body();
////
////                    ApiArtista apiArtista = vagalumeBusca.getArt();
////                    ApiItem apiMusica = vagalumeBusca.getMus().get(0);
//
//                    //Adicionar aos campos do xml
//                    nomeDaMusica.setText(vagalumeBusca.getMus().get(0).getName());
//                    nomeDoArtista.setText(vagalumeBusca.getArt().getName());
//                    letraDaMusica.setText(vagalumeBusca.getMus().get(0).getText());
//                    Picasso.get().load(vagalumeBusca.getArt().getUrl()+"images/profile.jpg").into(imagemArtista);
//
//                }else {Log.e(TAG, " onResponse: "+response.errorBody());}
//            }
//
//            @Override
//            public void onFailure(Call<VagalumeBusca> call, Throwable t){Log.e(TAG, " onFailure: "+t.getMessage());}
//        });
    }



}
