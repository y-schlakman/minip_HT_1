package elements;

//import geometries.AABB;

import java.util.LinkedList;

public class AABBtree {

    //Enum used to switch through axis of separation per tree generation.
    enum Axis {X_AXIS, Y_AXIS, Z_AXIS}

    //Private helper methode to use 'Axis' enum more elegantly.
    private double axisValue(Point3D p, Axis axis){
        if(axis == Axis.X_AXIS)
            return p.getCoord().getX();

        if(axis == Axis.Y_AXIS)
            return p.getCoord().getY();

        return p.getCoord().getZ();
    }

    private Node root;

    private class Node{

        private Axis _axis;

        //Left and righ sons respectively.
        private Node left;
        private Node right;

        //The AABB around all sub-boxes of this node.
        private AABB box;

        //List of all sub-AABB's left to divide.
        private List<AABB> subBoxes;

        public Node(List<AABB> boxes, Axis axis = Axis.X_AXIS){
            box = makeAABB(boxes);
            subBoxes = boxes;

            subBoxes.sort(_axis);

            if(subBoxes.size() <= 1){
                left = null;
                right = null;
                return;
            }

            int mid = subBoxes.size()/2;

            List<AABB> leftBoxes = new LinkedList<AABB>();
            for(int i = 0; i<mid ; ++i)
                leftBoxes.add(subBoxes.get(i));

            List<AABB> rightBoxes = new LinkedList<AABB>();
            for(int i = mid; i<subBoxes.size() ; ++i)
                rightBoxes.add(subBoxes.get(i));

            Axis nextAxis = _axis==Axis.X_AXIS?Axis.Y_AXIS:_axis==Axis.Y_AXIS?Axis.Z_AXIS:Axis.X_AXIS;

            left = new Node(leftBoxes, nextAxis);
            left = new Node(leftBoxes, nextAxis);
        }
    }

    public AABBtree(List<AABB> boxes){
        if(boxes == null) {
            root = null;
            return;
        }
        root = new Node(boxes);
    }

}
