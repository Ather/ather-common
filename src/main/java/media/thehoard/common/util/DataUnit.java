/**
 * 
 */
package media.thehoard.common.util;

/**
 * @author Michael Haas
 *
 */
public enum DataUnit {
	b(1),
	Bit(b),
	Kb(1000),
	Kilobit(Kb),
	Mb(1000 * 1000),
	Megabit(Mb),
	Gb(1000 * 1000 * 1000),
	Gigabit(Gb),
	Tb(1000 * 1000 * 1000 * 1000),
	Terabit(Tb),
	Pb(1000 * 1000 * 1000 * 1000 * 1000),
	Petabit(Pb),
	
	B(8),
	Byte(B),
	KB(8 * 1000),
	Kilobyte(KB),
	MB(8 * 1000 * 1000),
	Megabyte(MB),
	GB(8 * 1000 * 1000 * 1000),
	Gigabyte(GB),
	TB(8 * 1000 * 1000 * 1000 * 1000),
	Terabyte(TB),
	PB(8 * 1000 * 1000 * 1000 * 1000 * 1000),
	Petabyte(PB),
	
	KiB(8 * 1024),
	Kibibyte(KiB),
	MiB(8 * 1024 * 1024),
	Mebibyte(MiB),
	GiB(8 * 1024 * 1024 * 1024),
	Gibibyte(GiB),
	TiB(8 * 1024 * 1024 * 1024 * 1024),
	Tebibyte(TiB),
	PiB(8 * 1024 * 1024 * 1024 * 1024 * 1024),
	Pebibyte(PiB);
	
	private double bitValue;
	
	DataUnit(double bitValue) {
		this.bitValue = bitValue;
	}
	
	DataUnit(DataUnit unitEquivalent) {
		this(unitEquivalent.bitValue);
	}
	
	/* *bits */
	
	public double toBits(double value) {
		return (bitValue*value)/b.bitValue;
	}
	
	public double toKilobits(double value) {
		return (bitValue*value)/Kb.bitValue;
	}
	
	public double toMegabits(double value) {
		return (bitValue*value)/Mb.bitValue;
	}
	
	public double toGigabits(double value) {
		return (bitValue*value)/Gb.bitValue;
	}
	
	public double toTerabits(double value) {
		return (bitValue*value)/Tb.bitValue;
	}
	
	public double toPetabits(double value) {
		return (bitValue*value)/Pb.bitValue;
	}
	
	/* *bytes */
	
	public double toBytes(double value) {
		return (bitValue*value)/B.bitValue;
	}

	public double toKilobytes(double value) {
		return (bitValue*value)/KB.bitValue;
	}

	public double toMegabytes(double value) {
		return (bitValue*value)/MB.bitValue;
	}

	public double toGigabytes(double value) {
		return (bitValue*value)/GB.bitValue;
	}

	public double toTerabytes(double value) {
		return (bitValue*value)/TB.bitValue;
	}

	public double toPetabytes(double value) {
		return (bitValue*value)/PB.bitValue;
	}
	
	/* *bibytes */
	
	public double toKibibytes(double value) {
		return (bitValue*value)/KiB.bitValue;
	}
	
	public double toMebibytes(double value) {
		return (bitValue*value)/MiB.bitValue;
	}
	
	public double toGibibytes(double value) {
		return (bitValue*value)/GiB.bitValue;
	}
	
	public double toTebibytes(double value) {
		return (bitValue*value)/TiB.bitValue;
	}
	
	public double toPebibytes(double value) {
		return (bitValue*value)/PiB.bitValue;
	}
}
