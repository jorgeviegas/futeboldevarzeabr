package br.com.sharkweb.fbv;

/**
 * Created by Tiago on 26/08/2015.
 */
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;

@SuppressLint("NewApi")
public class GolsFragmentTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_gols, container, false);
        return rootView;
    }
}