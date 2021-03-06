
package net.sf.marineapi.nmea.sentence;

import net.sf.marineapi.nmea.util.CompassPoint;
import net.sf.marineapi.nmea.util.DataStatus;
import net.sf.marineapi.nmea.util.FaaMode;

/**
 * Recommended minimum navigation information type C. Current time and date,
 * position, speed over ground, true course over ground and magnetic variation.
 * <p>
 * Example:<br>
 * <code>$GPRMC,120044,A,6011.552,N,02501.941,E,000.0,360.0,160705,006.1,E*7C</code>
 * 
 * @author Kimmo Tuukkanen
 */
public interface RMCSentence extends PositionSentence, TimeSentence,
		DateSentence {

	/**
	 * Get the corrected course over ground. Correction is done by subtracting
	 * or adding the magnetic variation from true course (easterly variation
	 * subtracted and westerly added).
	 * 
	 * @return Corrected true course
	 * @see #getCourse()
	 * @see #getVariation()
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If course or
	 *             variation data is not available.
	 * @throws net.sf.marineapi.parser.ParseException If course or variation
	 *             field contains unexpected or illegal value.
	 */
	double getCorrectedCourse();

	/**
	 * Get true course over ground (COG).
	 * 
	 * @return True course in degrees
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	double getCourse();

	/**
	 * Get the direction of magnetic variation; east or west.
	 * 
	 * @return Direction.EAST or Direction.WEST
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	CompassPoint getDirectionOfVariation();

	/**
	 * Get the FAA operating mode for GPS.
	 * 
	 * @return FaaMode enum
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	FaaMode getMode();

	/**
	 * Get current speed over ground (SOG).
	 * 
	 * @return Speed in knots (nautical miles per hour).
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	double getSpeed();

	/**
	 * Gets the data status, valid or invalid.
	 * 
	 * @return {@link DataStatus#ACTIVE} or {@link DataStatus#VOID}
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	DataStatus getStatus();

	/**
	 * Get the magnetic variation. Easterly variation subtracts from true
	 * course, and is thus returned as negative value. Otherwise, the value is
	 * positive.
	 * 
	 * @return Magnetic variation in degrees
	 * @throws net.sf.marineapi.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	double getVariation();

	/**
	 * Set true course over ground (COG).
	 * 
	 * @param cog True course in degrees
	 */
	void setCourse(double cog);

	/**
	 * Set the direction of magnetic variation, east or west.
	 * 
	 * @param dir {@link CompassPoint#EAST} or {@link CompassPoint#WEST}
	 * @throws IllegalArgumentException If specified Direction is other than
	 *             defined as valid for param <code>dir</code>.
	 */
	void setDirectionOfVariation(CompassPoint dir);

	/**
	 * Set the FAA operation mode of GPS.
	 * 
	 * @param mode Mode to set
	 */
	void setMode(FaaMode mode);

	/**
	 * Set current speed over ground (SOG).
	 * 
	 * @param sog Speed in knots (nautical miles per hour).
	 */
	void setSpeed(double sog);

	/**
	 * Set the data status, valid or invalid.
	 * 
	 * @param status {@link DataStatus#ACTIVE} or {@link DataStatus#VOID}
	 */
	void setStatus(DataStatus status);

	/**
	 * Set the magnetic variation.
	 * 
	 * @param var Magnetic variation in degrees
	 */
	void setVariation(double var);
}
