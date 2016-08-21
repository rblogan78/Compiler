package Compiler;

/**
 *  This enum contains all the different classes of Node within the Syntax tree
 * @author Mike Hannaford
 */
public enum Node {
    NUNDEF, NPROG,NGLOB, NILIST, NINIT,	NFUNCS, NMAIN,	NSDLST,	NTYPEL,	NRTYPE,
    NATYPE, NFLIST, NSDECL, NALIST, NARRD, NFUND, NPLIST, NSIMP, NARRP,	NARRC,
    NDLIST, NSTATS, NFOR, NREPT, NASGNS, NIFTH,	NIFTE,	NASGN,	NINPUT,	NOUTP,
    NLINE, NCALL, NRETN, NVLIST, NSIMV,	NARRV,	NEXPL,	NBOOL,	NNOT,	NAND,
    NOR, NXOR, NEQL, NNEQ, NGTR, NLSS, NLEQ, NADD, NSUB, NMUL, NDIV, NMOD, NPOW,
    NILIT, NFLIT, NTRUE, NFALS,	NFCALL,	NPRLST,	NSTRG,	NGEQ  
};
