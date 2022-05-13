package com.example.coronatracker.controller;

import com.example.coronatracker.models.LocationStatus;
import com.example.coronatracker.services.CoronaVirusDataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    final
    CoronaVirusDataServer coronaVirusDataServer;

    public HomeController( CoronaVirusDataServer coronaVirusDataServer) {
        this.coronaVirusDataServer = coronaVirusDataServer;
    }

    @GetMapping("/")
    public String home(Model model){
        List<LocationStatus> allStatus = coronaVirusDataServer.getAllStatus();
        /*
        Lo que hace la totalReporteCases es que toma la lista de objetos
        los combierte en un stream y luego los mapea en un Integer cada uno de los objetos
        y al final suma cada uno de estos  Integers
        * */
        int totalReportedCases = allStatus.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStatus.stream().mapToInt(stat -> stat.getDiffFromPreviousDay( )).sum();
        model.addAttribute("locationStatus", allStatus);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "home";
    }
}
