package com.fantasticfive.shared;

import java.io.Serializable;

public interface IBuilding extends Serializable {
   void setLocation(Point location);
   Point getLocation();
   void setOwner(IPlayer owner);
   IPlayer getOwner();
   boolean damageHealth(int hp);
}
