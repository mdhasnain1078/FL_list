package com.example.fllistapp.repository;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.fllistapp.di.Interfaces.InternshipRepositoryInter;
import com.example.fllistapp.model.Internship;
import com.example.fllistapp.model.InternshipResponse;
import com.bluelinelabs.logansquare.LoganSquare;
import java.io.IOException;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InternshipRepository implements InternshipRepositoryInter {

    private static final String API_URL = "https://internshala.com/flutter_hiring/search";
    private final RequestQueue requestQueue;

    @Inject
    public InternshipRepository(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void fetchInternships(InternshipCallback callback) {
        StringRequest request = new StringRequest(Request.Method.GET, API_URL,
                response -> {
                    try {
                        InternshipResponse internshipResponse = LoganSquare.parse(response, InternshipResponse.class);
                        ArrayList<Internship> internshipsList = new ArrayList<>();

                        for (int id : internshipResponse.internshipIds) {
                            Internship internship = internshipResponse.internshipsMeta.get(String.valueOf(id));
                            if (internship != null) {
                                internshipsList.add(internship);
                            }
                        }

                        callback.onSuccess(internshipsList);
                    } catch (IOException e) {
                        callback.onError("Parsing error: " + e.getMessage());
                    }
                },
                error -> callback.onError("Volley error: " + error.getMessage()));

        requestQueue.add(request);
    }
}

