package com.onvaou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Nicolas on 04/11/2017.
 */

public class FragmentDetail extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int id = this.getArguments().getInt("id");
        ArrayList<Bar> bars = SharedPreferencesHelper.getInstance(getContext()).RecupererListeBars();
        Bar b = bars.get(id);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tvNom = (TextView) view.findViewById(R.id.tvNom);
        TextView tvNote = (TextView) view.findViewById(R.id.tvNote);
        TextView tvTheme = (TextView) view.findViewById(R.id.tvTheme);
        TextView tvPrix = (TextView) view.findViewById(R.id.tvPrix);
        TextView tvAdresse = (TextView) view.findViewById(R.id.tvAdresse);

        DecimalFormat df = new DecimalFormat("#.00");
        tvNom.setText(b.getNom());
        tvNote.setText( "Note = " + df.format(b.getNote()));
        tvTheme.setText(b.getTheme().name());
        tvPrix.setText(b.getPrix().name());
        tvAdresse.setText(b.getNumero()+" "+ b.getRue() + "\n" + b.getCP() +"-"+b.getVille());

        final ImageView ivFavori = (ImageView) view.findViewById(R.id.ivFavori);
        if(b.isFavori())
            ivFavori.setImageResource(R.mipmap.favori);
        else
            ivFavori.setImageResource(R.mipmap.favorinon);

        ivFavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ivFavori.getDrawable().getConstantState() == getResources().getDrawable(R.mipmap.favori,null).getConstantState())
                {
                    ivFavori.setImageResource(R.mipmap.favorinon);
                } else {
                    ivFavori.setImageResource(R.mipmap.favori);
                    Toast.makeText(getContext(),"Ajouté aux Favoris", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final double lat = b.getLatitude();
        final double lon = b.getLongitude();

        MapView mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                GoogleMap googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        final RatingBar rbNote = (RatingBar) view.findViewById(R.id.rbNote);
        Button btNote = (Button) view.findViewById(R.id.btNoter);

        btNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),String.valueOf(rbNote.getRating()), Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

}
