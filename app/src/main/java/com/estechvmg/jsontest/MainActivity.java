package com.estechvmg.jsontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String jsonString="[{\"name\": \"Molecule Man\",\"age\": 29,\"secretIdentity\": \"Dan Jukes\",\"powers\": [\"Radiation resistance\",\"Turning tiny\",\"Radiation blast\"]},{\"name\": \"Madame Uppercut\",\"age\": 39,\"secretIdentity\": \"Jane Wilson\",\"powers\": [\"Million tonne punch\",\"Damage resistance\",\"Superhuman reflexes\"]},{\"name\": \"Eternal Flame\",\"age\": 1000000,\"secretIdentity\": \"Unknown\",\"powers\": [\"Immortality\",\"Heat Immunity\",\"Inferno\",\"Teleportation\",\"Interdimensional travel\"]}]";
    public TextView out1,out2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        out1=findViewById(R.id.textView1);
        out2=findViewById(R.id.textView2);
        List<SuperHero> superHeroList=new ArrayList<SuperHero>();
        //First JSON (arrayed one)
        firstJSON();
        //Second JSON (in assets)
        secondJSON();
        //Set TextView Text
        try {

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName + ".json"); //Objeto InputStream donde se almacena un flujo de bytes
            int size = is.available(); // se obtiene el tamaño del InputStream de nuestro archivo
            byte[] buffer = new byte[size]; // creamos un array de byte con el tamaño anterior
            is.read(buffer); // lee cada byte del InputStream y lo almacena en el array
            is.close(); // se cierra el InputStream
            json = new String(buffer, StandardCharsets.UTF_8); //Creamos un String a partir del buffer, el segundo parámetro indica el formato de codificación UTF_8
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void secondJSON() {
        try {
            JSONObject jsonObject=new JSONObject(loadJSONFromAsset("batman"));
            int id = jsonObject.getInt("id");
            String name=jsonObject.getString("name");

            JSONObject powerStatsObject=jsonObject.getJSONObject("powerstats");
            PowerStats powerStats=new PowerStats(
                    powerStatsObject.getInt("intelligence"),
                    powerStatsObject.getInt("strength"),
                    powerStatsObject.getInt("speed"),
                    powerStatsObject.getInt("durability"),
                    powerStatsObject.getInt("power"),
                    powerStatsObject.getInt("combat")
                    );

            JSONObject biographyObject=jsonObject.getJSONObject("biography");
            String fullName=biographyObject.getString("full-name");
            String alterEgos=biographyObject.getString("alter-egos");
            JSONArray aliasesJson=biographyObject.getJSONArray("aliases");
            String[] aliases=new String[]{
              aliasesJson.getString(0),
              aliasesJson.getString(1)
            };
            String placeOfBirth = biographyObject.getString("place-of-birth");
            String firstAppearance = biographyObject.getString("first-appearance");
            String publisher = biographyObject.getString("publisher");
            String alignment = biographyObject.getString("alignment");
            Biography biography=new Biography(fullName,alterEgos,aliases,placeOfBirth,firstAppearance,publisher,alignment);

            JSONObject appearanceObject=jsonObject.getJSONObject("appearance");
            String gender=appearanceObject.getString("gender");
            String race=appearanceObject.getString("race");
            JSONArray heightJson=appearanceObject.getJSONArray("height");
            String[] height=new String[]{
                    heightJson.getString(0),
                    heightJson.getString(1)
            };
            JSONArray weightJson=appearanceObject.getJSONArray("weight");
            String[] weight=new String[]{
                    weightJson.getString(0),
                    weightJson.getString(1)
            };
            String eyeColor=appearanceObject.getString("eye-color");
            String hairColor=appearanceObject.getString("hair-color");
            Appearance appearance=new Appearance(gender,race,height,weight,eyeColor,hairColor);
            Image image=new Image(jsonObject.getJSONObject("image").getString("url"));

            Hero hero=new Hero(id,name,powerStats,biography,appearance,image);
            out2.setText(hero.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void firstJSON(){
        try {
            JSONArray jsonArray=new JSONArray(jsonString);
            SuperHero []hero = new SuperHero[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++){
                JSONObject heroObject=jsonArray.getJSONObject(i);
                String name=heroObject.getString("name");
                int age=heroObject.getInt("age");
                String secretIdentity=heroObject.getString("secretIdentity");
                JSONArray powersArray=heroObject.getJSONArray("powers");
                String[] powers=new String[powersArray.length()];
                for(int a=0;a!=powersArray.length();a++){
                    powers[a]=powersArray.getString(a);
                }
                hero[i]=new SuperHero(name,age,secretIdentity,powers);
            }
            String text="";
            for(int b=0;b!=hero.length;b++){
                text+=hero[b].toString() + "\n";
            }
            out1.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
class SuperHero{
    public String name,secretIdentity;
    public int age;
    public String[] powers;
    SuperHero(String name, int age, String secretIdentity, String[] powers){
        this.name=name;
        this.age=age;
        this.secretIdentity=secretIdentity;
        this.powers=powers;
    }

    @NonNull
    @Override
    public String toString() {
        String out="Name: " + name + "\nAge: " + age + "\nSecret Identity:" + secretIdentity + "\nPowers";
        for(int a=0;a!=powers.length;a++){
            out+="\n" + powers[a];
        }
        return out;
    }
}
class Hero{
    int id;
    String name;
    PowerStats powerStats;
    Biography biography;
    Appearance appearance;
    Image image;
    Hero(int id,String name, PowerStats powerStats, Biography biography, Appearance appearance, Image image){
        this.id=id;
        this.name=name;
        this.powerStats=powerStats;
        this.biography=biography;
        this.appearance=appearance;
        this.image=image;
    }

    @NonNull
    @Override
    public String toString() {
        return    "\nID:" + id
                + "\nName:" + name
                + "\nPowerStats:"
                + "\n   Intelligence:" + powerStats.intelligence
                + "\n   Strength:" + powerStats.strength
                + "\n   Speed:" + powerStats.speed
                + "\n   Durability:" + powerStats.durability
                + "\n   Power:" + powerStats.power
                + "\n   Combat:" + powerStats.combat
                + "\nBiography:"
                + "\n   Full Name:" + biography.fullName
                + "\n   Alter Egos:" + biography.alterEgos
                + "\n   Aliases:"
                + "\n       " + biography.aliases[0]
                + "\n       " + biography.aliases[1]
                + "\n   Place Of Birth:" + biography.placeOfBirth
                + "\n   First Appearance:" +  biography.firstAppearance
                + "\n   Publisher:" + biography.publisher
                + "\n   Alignment:" + biography.alignment
                + "\nAppearance:"
                + "\n   Gender:" + appearance.gender
                + "\n   Race:" + appearance.race
                + "\n   Height:"
                + "\n       " + appearance.height[0]
                + "\n       " + appearance.height[1]
                + "\n   Weight:"
                + "\n       " + appearance.weight[0]
                + "\n       " + appearance.weight[1]
                + "\n   Eye Color:" + appearance.eyeColor
                + "\n   Hair Color:" + appearance.hairColor
                + "\nImage:"
                + "\n   URL:" + image.url;
    }
}
class Biography{
    String fullName,placeOfBirth,firstAppearance,publisher,alignment,alterEgos;
    String[] aliases;
    Biography(String fullName, String alterEgos,String[] aliases, String placeOfBirth,String firstAppearance,String publisher,String alignment){
        this.fullName=fullName;
        this.alterEgos=alterEgos;
        this.aliases=aliases;
        this.placeOfBirth=placeOfBirth;
        this.firstAppearance=firstAppearance;
        this.publisher=publisher;
        this.alignment=alignment;
    }
}
class PowerStats{
    int intelligence, strength,speed,durability,power,combat;
    PowerStats(int intelligence,int strength, int speed, int durability, int power, int combat){
        this.intelligence=intelligence;
        this.strength=strength;
        this.speed=speed;
        this.durability=durability;
        this.power=power;
        this.combat=combat;
    }
}
class Appearance{
    String gender;
    String race;
    String[] height;
    String[] weight;
    String eyeColor;
    String hairColor;
    Appearance(String gender,String race,String[] height,String[] weight, String eyeColor,String hairColor){
        this.gender=gender;
        this.race=race;
        this.height = height;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
    }
}
class Image{
    String url;
    Image(String url){
        this.url=url;
    }
}
