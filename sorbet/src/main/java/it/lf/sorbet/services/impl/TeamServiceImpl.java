package it.lf.sorbet.services.impl;

import it.lf.sorbet.services.TeamService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeamServiceImpl implements TeamService {

    public String getTeamIdByAlias(String alias) {
        for (String id : teamMap.keySet()) {
            List<String> aliases = teamMap.get(id);
            if (aliases.contains(alias)) {
                return id;
            }
        }
        return null;
    }

    private static final Map<String, List<String>> teamMap;
    static
    {
        teamMap = new HashMap<String, List<String>>();
        teamMap.put("Torino", Arrays.asList("Torino"));
        teamMap.put("Cagliari", Arrays.asList("Cagliari"));
        teamMap.put("SSC Napoli", Arrays.asList("SSC Napoli", "Napoli"));
        teamMap.put("Lazio Rome", Arrays.asList("Lazio Rome", "Lazio"));
        teamMap.put("Pescara", Arrays.asList("Pescara"));
        teamMap.put("FC Empoli", Arrays.asList("FC Empoli", "Empoli"));
        teamMap.put("Udinese", Arrays.asList("Udinese"));
        teamMap.put("Palermo", Arrays.asList("Palermo"));
        teamMap.put("AC Milan", Arrays.asList("AC Milan", "Milan"));
        teamMap.put("Sassuolo", Arrays.asList("Sassuolo"));
        teamMap.put("Atalanta", Arrays.asList("Atalanta"));
        teamMap.put("Chievo", Arrays.asList("Chievo", "Chievo Verona"));
        teamMap.put("Juventus", Arrays.asList("Juventus"));
        teamMap.put("Fiorentina", Arrays.asList("Fiorentina", "Acf Fiorentina"));
        teamMap.put("Sampdoria", Arrays.asList("Sampdoria"));
        teamMap.put("Inter Milan", Arrays.asList("Inter Milan", "Inter",  "FC Internazionale"));
        teamMap.put("Crotone", Arrays.asList("Crotone"));
        teamMap.put("AS Roma", Arrays.asList("AS Roma", "Roma"));
        teamMap.put("Bologna", Arrays.asList("Bologna"));
        teamMap.put("Genoa CFC", Arrays.asList("Genoa CFC", "Genoa"));

    }



}
