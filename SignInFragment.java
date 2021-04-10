package com.daat.productivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.internal.$Gson$Preconditions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView signup,forgot_pass;
    private EditText siemail,sipass;
    private Button signinBtn;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        signup = view.findViewById(R.id.signupBtn);
        siemail = view.findViewById(R.id.sign_in_user_email);
        sipass = view.findViewById(R.id.sign_in_user_pass);
        signinBtn = view.findViewById(R.id.Sign_in_button);
        progressBar = view.findViewById(R.id.progressBar_signin);
        forgot_pass = view.findViewById(R.id.forgotPass);
        frameLayout = getActivity().findViewById(R.id.REGISTER_FRAMElAYOUT);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        firebaseAuth =FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),forgotPass.class);
                startActivity(intent);
            }
        });

        siemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sipass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chkemailAndPass();
            }
        });

    }



    private void chkInputs() {
        if (!TextUtils.isEmpty(siemail.getText())){
            if (!TextUtils.isEmpty(sipass.getText())){
                signinBtn.setEnabled(true);
                signinBtn.setTextColor(getActivity().getResources().getColor(R.color.white));
            }else{
                signinBtn.setEnabled(false);
            }
        }else {
            signinBtn.setEnabled(false);
        }
    }
    private void chkemailAndPass() {

        if (siemail.getText().toString().matches(emailPattern)){
            if (sipass.length()>=8){
                progressBar.setVisibility(View.VISIBLE);
                signinBtn.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(siemail.getText().toString(),sipass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getActivity(),DashboardActivity.class));
                            getActivity().finish();
                        }else {
                            signinBtn.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                signinBtn.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }else{
            signinBtn.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}