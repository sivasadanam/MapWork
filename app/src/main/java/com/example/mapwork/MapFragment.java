package com.example.mapwork;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public GoogleMap mMap;
    public String resposeString;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public String[] lat = new String[10];
    public String[] lon = new String[10];
    public ViewGroup viewGroup;
    public int len;
    public String[] museumsid = new String[10];
    public Float flat;
    public Float flet;

    TextView tv;



    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        viewGroup = view.findViewById(android.R.id.content);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
      //  postGet();
        return view;
    }

    public void postGet() {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://muzirisdemot2.herokuapp.com/ipa/onemuseum";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("id", "1101");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder().url(url).post(body).header("Content-Type", "application/json").build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                String mMessage = e.getMessage().toString();
                Log.e("Message","Failed.........................................................");
                //publishString = mMessage;

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String mMessage = response.body().string();
                //  publishString = mMessage;
                Log.e("Message","Got Response......................................................");
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // showMuzDetailsAsAlert();

                    }
                });


            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
            jsonRequest();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void jsonRequest() {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://muzirisdemot2.herokuapp.com/ipa/museums";
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               resposeString = "fail";
                Log.e("Message", "error in getting response using async okhttp call");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //ResponseBody responseBody = response.body();
                String data = response.body().string();
                Log.e("Message", "success in getting response using async okhttp call");
                //  Object obj = data;
                //  JSONArray jsonarray = (JSONArray) obj;
                //   System.out.println(jsonarray.length());
                resposeString = data;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           jsonParsing();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                System.out.println(response);
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }

                //Log.i(TAG,responseBody.string());
            }
        });

    }
    public void jsonParsing() throws JSONException {
        //  Object obj = resposeString;
        final Object obb[] =  new Object[10];
        Log.e("Message", resposeString);
        JSONArray jsonArr = new JSONArray(resposeString);
        len = jsonArr.length();
        for(int i = 0 ;i<len;i++){
            obb[i] = jsonArr.get(i);
            JSONObject jo = (JSONObject) obb[i];
            lat[i] = jo.get("lat").toString();
            lon[i] = jo.get("lon").toString();
            museumsid[i] = jo.get("id").toString();
//            place[i] = jo.get("place").toString();
//            desc[i] = jo.get("desc").toString();
//            mainimg[i] = jo.get("mainimg").toString();
//         //   pic1[i] = jo.get("pic1").toString();
//         //   pic2[i] = jo.get("pic2").toString();
//          //  pic3[i] = jo.get("pic3").toString();
//            contact[i] = jo.get("contact").toString();
//            avgtime[i] = jo.get("avgtime").toString();
//            highlight[i] = jo.get("highlight").toString();
//            name[i] = jo.get("name").toString();
//            priority[i] = jo.get("priority").toString();
         //   tv.setText(tv.getText() +" "+lat[i]+"   "+lon[i]+"   \n");

        }
        LatLng Mark[] = new LatLng[10];
        for(int i =0 ; i< len; i++){
            flat = Float.parseFloat(lat[i]);
            flet = Float.parseFloat(lon[i]);
            Mark[i] = new LatLng(flat, flet);
            //  LatLng Home = new LatLng( 9.540094, 76.331446);

            mMap.addMarker(new MarkerOptions()

                    .position(Mark[i]).title(museumsid[i].toString()));

        }

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng( flat, flet))
                .zoom(12)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
        mMap.setOnMarkerClickListener(this);
        //  System.out.println(jsonArr.length());
        //  JSONArray jsonarray = (JSONArray) obj;
        // Object obj2 = jsonArr.get(0);
        // JSONObject jo = (JSONObject) obj2;
        //  tv.setText(jo.get("name").toString());
//         lat = jo.get("lat").toString();
//         lon = jo.get("lon").toString();
//           flat = Float.parseFloat(lat);
//           flet = Float.parseFloat(lon);
//      //   tv.setText(lat+"   "+lon+"   "+len);
        //  txtString.setText(jsonArr.get(0).toString());
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
//        mapFragment.getMapAsync(MuzMap.this);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        // Toast.makeText(getContext(),marker.getTitle(),Toast.LENGTH_LONG).show();

        // showCustomDialog(Integer.valueOf(marker.toString()));


        if (marker.getTitle().equals(museumsid[0]))
        {

//            try {
                showCustomDialog();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            //handle click here
            Toast.makeText(getContext(),museumsid[0],Toast.LENGTH_LONG).show();
        }

        if (marker.getTitle().equals(museumsid[1]))
        {
            //handle click here
//            try {
                showCustomDialog();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Toast.makeText(getContext(),museumsid[1].toString(),Toast.LENGTH_LONG).show();
        }
        if (marker.getTitle().equals(museumsid[2]))
        {
            //handle click here
//            try {
               showCustomDialog();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Toast.makeText(getContext(),museumsid[2].toString(),Toast.LENGTH_LONG).show();
        }
        if (marker.getTitle().equals(museumsid[3]))
        {
            //handle click here
//            try {
              showCustomDialog();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            Toast.makeText(getContext(),museumsid[3].toString(),Toast.LENGTH_LONG).show();
        }

        return false;
    }
    public void showCustomDialog(){
//
//        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, viewGroup, false);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Guide List");
//        TextView tvv = dialogView.findViewById(R.id.textView3);
//       // ImageView image2 =(ImageView) dialogView.findViewById(R.id.imageView);
//        tvv.setText("GGGGG");


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.my_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setTitle("Guide List");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
