package com.fantasticfive.projecthex;

import com.fantasticfive.shared.Hexagon;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.Player;

import java.util.ArrayList;
import java.util.List;

public class Fog {


    private Player player;
    private List<Hexagon> visitedHexes;
    private List<Hexagon> neighbouringHexes;
    private Map map;

    public Fog(Player player, List<Hexagon> visitedHexes, Map map){
        this.visitedHexes = visitedHexes;
        this.map = map;
        neighbouringHexes = new ArrayList<>();
        calculateNeighbouringHexes(visitedHexes);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Hexagon> getVisitedHexes() {
        return visitedHexes;
    }

    public List<Hexagon> getNeighbouringHexes() {
        return neighbouringHexes;
    }

    public void calculateNeighbouringHexes(List<Hexagon> hexagons){
        for (Hexagon hex : hexagons){
            for (Hexagon neighbourHex : map.hexesInCirle(hex.getLocation(), 1)){
                if (!isVisisted(neighbourHex) && !isNeighbour(neighbourHex)){
                    neighbouringHexes.add(neighbourHex);
                }
            }
        }
    }

    public void calculateNeighbouringHexes(Hexagon hexagon){
        for (Hexagon neighbourHex : map.hexesInCirle(hexagon.getLocation(), 1)){
            if (!isVisisted(neighbourHex) && !isNeighbour(neighbourHex)){
                neighbouringHexes.add(neighbourHex);
            }
        }
    }

    public void unitMovement(Hexagon oldHex, Hexagon newHex){
        //Define path, add all hexes along this path to visitedHexes.
        for (Hexagon hex : map.getPath(oldHex, newHex)){
            if (!isVisisted(hex)){
                visitedHexes.add(hex);
                calculateNeighbouringHexes(hex);
                if (isNeighbour(hex)){
                    removeNeighbour(hex);
                }
            }
        }
    }

    public void removeNeighbour(Hexagon hex){
        Hexagon removeThisHex = hex;
        for (Hexagon h : neighbouringHexes){
            if (h.getLocation().getX() == hex.getLocation().getX() && h.getLocation().getY() == hex.getLocation().getY()){
                removeThisHex = h;
            }
        }
        neighbouringHexes.remove(removeThisHex);
    }

    public boolean isVisisted(Hexagon hexagon){
        for (Hexagon visistedHexagon : visitedHexes){
            if (visistedHexagon.getLocation().getX() == hexagon.getLocation().getX() && visistedHexagon.getLocation().getY() == hexagon.getLocation().getY()){
                return true;
            }
        }
        return false;
    }

    public boolean isNeighbour(Hexagon hexagon){
        for (Hexagon neighbourHexagon : neighbouringHexes){
            if (neighbourHexagon.getLocation().getX() == hexagon.getLocation().getX() && neighbourHexagon.getLocation().getY() == hexagon.getLocation().getY()){
                return true;
            }
        }
        return false;
    }

    public void unitCreated(Hexagon newHex){
        visitedHexes.add(newHex);
        calculateNeighbouringHexes(newHex);
        if (isNeighbour(newHex)){
            Hexagon removeThisHex = newHex;
            for (Hexagon h : neighbouringHexes){
                if (h.getLocation().getX() == newHex.getLocation().getX() && h.getLocation().getY() == newHex.getLocation().getY()){
                    removeThisHex = h;
                }
            }
            neighbouringHexes.remove(removeThisHex);
        }
    }

}
