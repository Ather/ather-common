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
	Mb(1000000),
	Megabit(Mb),
	Gb(1000000000),
	Gigabit(Gb),
	Tb(1000000000000d),
	Terabit(Tb),
	Pb(100000000000000d),
	Petabit(Pb),
	
	B(8),
	Byte(B),
	KB(8000),
	Kilobyte(KB),
	MB(8000000),
	Megabyte(MB),
	GB(8000000000d),
	Gigabyte(GB),
	TB(8000000000000d),
	Terabyte(TB),
	PB(80000000000000d),
	Petabyte(PB),
	
	KiB(8192),
	Kibibyte(KiB),
	MiB(8389000),
	Mebibyte(MiB),
	GiB(8590000000d),
	Gibibyte(GiB),
	TiB(8796000000000d),
	Tebibyte(TiB),
	PiB(9007000000000000d),
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
