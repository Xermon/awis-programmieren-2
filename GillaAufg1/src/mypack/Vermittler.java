/**
 * Jan Gilla
 * 09.03.2017
 * V1.0
 * 
 * Abstrakte Klasse eines Vermittlers.
 */

package mypack;

public abstract class Vermittler {

	public abstract void vermitteln(Zuord z);
	public abstract void vermitteln(Zuord z, Kollege k);
	
	public abstract void add(Kollege k);
	public abstract Boolean find(Kollege k);
}
