package com.example.base.audio.tuning

val tunings = setOf(
    TuningConfig(
        "Standard",
        arrayOf(
            NoteConfig("E", 82.41),
            NoteConfig("A", 110.0),
            NoteConfig("D", 146.8),
            NoteConfig("G", 196.0),
            NoteConfig("B", 246.9),
            NoteConfig("E", 329.6)
        )
    ),
    TuningConfig(
        "DADGAD",
        arrayOf(
            NoteConfig("D", 73.4),
            NoteConfig("A", 110.0),
            NoteConfig("D", 146.8),
            NoteConfig("G", 196.0),
            NoteConfig("A", 220.0),
            NoteConfig("D", 293.7)
        )
    ),
    TuningConfig(
        "Drop D",
        arrayOf(
            NoteConfig("D", 73.4),
            NoteConfig("A", 110.0),
            NoteConfig("D", 146.8),
            NoteConfig("G", 196.0),
            NoteConfig("B", 246.9),
            NoteConfig("E", 329.6)
        )
    )
)

data class TuningConfig(
    val name: String,
    val notes: Array<NoteConfig>
) {
    init {
        if (notes.size != 6) throw IllegalArgumentException("Incorrect Tuning config: only 6 notes supported")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TuningConfig

        if (name != other.name) return false
        if (!notes.contentEquals(other.notes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + notes.contentHashCode()
        return result
    }
}

data class NoteConfig(
     val name: String,
     val hertz: Double
)