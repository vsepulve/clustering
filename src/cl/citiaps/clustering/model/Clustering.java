
package cl.citiaps.clustering.model;

import java.util.List;

public interface Clustering {
	public void execute(List<Data> data, Distance dist);
}
