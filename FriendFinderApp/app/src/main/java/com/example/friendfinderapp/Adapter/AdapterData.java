package com.example.friendfinderapp.Adapter;

import android.app.FragmentHostCallback;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfinderapp.Model.User_Model;
import com.example.friendfinderapp.R;

import java.util.List;

public class AdapterData{
    private Context ctx; //wakil activity di class non activity
    private List<User_Model> listDataUser;

    public AdapterData(Context ctx, List<User_Model> listDataUser) {
        this.ctx = ctx;
        this.listDataUser = listDataUser;
    }

    public class HolderData {

    }
}

