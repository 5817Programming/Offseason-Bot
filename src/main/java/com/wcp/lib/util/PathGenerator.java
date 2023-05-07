package com.wcp.lib.util;


import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.wcp.frc.subsystems.Swerve;
import com.wcp.lib.geometry.Rotation2d;
import com.wcp.lib.geometry.Translation2d;
import com.wcp.lib.geometry.HeavilyInspired.Edge;
import com.wcp.lib.geometry.HeavilyInspired.Node;
import com.wcp.lib.geometry.HeavilyInspired.Obstacle;
import com.wcp.lib.geometry.HeavilyInspired.VisGraph;

import java.util.ArrayList;
import java.util.List;

/** Custom PathPlanner version of SwerveControllerCommand */
public class PathGenerator {
    public static PathPlannerTrajectory generatePath(PathConstraints constraints,Node endTarget, List<Obstacle> obstacles){
        List<Node> fullPath = new ArrayList<Node>();

        Node start = new Node(Swerve.getInstance());
        VisGraph aStar = VisGraph.getInstance();
        List<PathPoint> fullPathPoints = new ArrayList<PathPoint>();

        aStar.addNode(endTarget);
    //SetUp AStar Map
    aStar.addNode(new Node(2.92-0.42,1.51-0.42));
    aStar.addNode(new Node(2.92-0.42,3.98+0.42));
    aStar.addNode(new Node(4.86+0.42,3.98+0.42));
    aStar.addNode(new Node(4.86+0.42,1.51-0.42));

    aStar.addNode(new Node(11.68-0.42,1.51-0.42));
    aStar.addNode(new Node(11.65,5));
    aStar.addNode(new Node(14.37,4.55));
    aStar.addNode(new Node(14.23,1.51-0.42));

    //aStar.addNode(new Node(14.07,4.50));


    aStar.addNode(new Node(1.22-0.42, 5.34-0.42));
    aStar.addNode(new Node(1.22-0.42, 5.70+0.42));
    aStar.addNode(new Node(3.26+0.42, 5.34+0.42));
    aStar.addNode(new Node(3.26+0.42, 5.7-0.42));

    aStar.addNode(new Node(13.22, 5.70));
    aStar.addNode(new Node(13.22, 5.34));
    aStar.addNode(new Node(15.39, 5.34));
    aStar.addNode(new Node(15.39, 5.7));

    for(int i = 0; i<aStar.getNodeSize();i++){
      Node startNode = aStar.getNode(i);
      //System.out.println(""+startNode.getX()+","+startNode.getY());
      for(int j = 0; j<aStar.getNodeSize(); j++){
        aStar.addEdge(new Edge(startNode, aStar.getNode(j)), obstacles);
      }
    }

        if(aStar.addEdge(new Edge(start, endTarget), obstacles)){
            fullPath.add(0,start);
            fullPath.add(1,endTarget);

        }else{
            for(int i = 0; i<aStar.getNodeSize();i++){
                Node end = aStar.getNode(i);
                aStar.addEdge(new Edge(start,end), obstacles);
            }
            fullPath = aStar.findPath(start, endTarget);
        }
        edu.wpi.first.math.geometry.Rotation2d Heading = new Rotation2d(fullPath.get(1).getX()-start.getX(),fullPath.get(1).getY()-start.getY()).toWPI();

        for(int i = 0; i < fullPath.size(); i++){
            if(i == 0){
                fullPathPoints.add(new PathPoint(start.getTranslation().toWPI(), Heading ,start.getHolRot()));
            }
            else if( i + 1 == fullPath.size()){
                edu.wpi.first.math.geometry.Translation2d translation = new Translation2d(endTarget.getTranslation()).toWPI();
                edu.wpi.first.math.geometry.Rotation2d heading = new Rotation2d(fullPath.get(i).getX() - fullPath.get(i - 1).getX(), endTarget.getY() - fullPath.get(i - 1).getY()).toWPI();
                fullPathPoints.add(i,new PathPoint(translation, heading, endTarget.getHolRot()));
            }
            else{
                edu.wpi.first.math.geometry.Translation2d translation = new Translation2d(fullPath.get(i).getTranslation().getX(),fullPath.get(i).getTranslation().getY()).toWPI();
                edu.wpi.first.math.geometry.Rotation2d heading = new Rotation2d(fullPath.get(i).getX() - fullPath.get(i - 1).getX(), fullPath.get(i).getY() - fullPath.get(i - 1).getY()).toWPI();
                fullPathPoints.add(i,new PathPoint(translation, heading, endTarget.getHolRot()));
            }
            
        }
        return PathPlanner.generatePath(constraints,fullPathPoints);
    }
   
    
}