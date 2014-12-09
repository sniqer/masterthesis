package MasterThesisPackage;

import java.awt.Shape;

import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

public class myXYLineAndShapeRenderer extends XYLineAndShapeRenderer {
   private static final long serialVersionUID = 1L; // <- eclipse insists on this and I hate warnings ^^

   @Override
   protected void addEntity(EntityCollection entities, Shape area, XYDataset dataset, int series, int item, double entityX, double entityY) {
      if(area.getBounds().width < 2 || area.getBounds().height < 2) 
    	  super.addEntity(entities, null, dataset, series, item, entityX, entityY);
      else 
    	  super.addEntity(entities, area, dataset, series, item, entityX, entityY);
   }
}