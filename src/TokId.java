/**
 * Created by Rob on 28/07/16.
 */
public enum TokId {
    TEOF,   // End of File Token
    // The 31 keywords come first
    TCD16, TCONS, TTYPS, TISKW, TARRS, TMAIN, TBEGN, TENDK, TARRY, TOFKW, TFUNC,
    TVOID, TCNST, TINTG, TREAL, TBOOL, TFORK, TREPT, TUNTL, TIFKW, TELSE, TINKW,
    TOUTP, TOUTL, TRETN, TNOTK, TANDK, TORKW, TXORK, TTRUE, TFALS,
    // then the operators and delimiters
    TLBRK, TRBRK, TLPAR, TRPAR, TSEMI, TCOMA, TCOLN, TDOTT, TASGN, TINPT,
    TDEQL, TNEQL, TGRTR, TLEQL, TLESS, TGREQ, TADDT, TSUBT, TMULT, TDIVT, TPERC, TCART,
    // then the tokens which need tuple values
    TIDNT, TILIT, TFLIT, TSTRG, TUNDF
};
