import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.iryndin.jdbf.core.DbfFileTypeEnum;
import net.iryndin.jdbf.core.DbfMetadata;
import net.iryndin.jdbf.core.DbfRecord;
import net.iryndin.jdbf.reader.DbfReader;
import net.iryndin.jdbf.util.DbfMetadataUtils;
import net.iryndin.jdbf.util.JdbfUtils;
import net.iryndin.jdbf.writer.DbfWriter;


public class JdbfMain {

	static String gdsString = "KONTR,C,1,0|N_MDP,C,8,0|W_LIST_NO,N,2,0|G32,N,3,0|N_RECEIVER,N,1,0|G33,C,10,0|G312,C,250,0|G35,N,13,2|G311,C,9,0|G318,C,14,0|G315,N,11,2|G317C,C,3,0|G221,C,3,0|G221_BUK,C,3,0|G42,N,15,2|KODS_PT,C,3,0|KODS_ABC2,C,2,0|N_TTH,C,30,0|G442REGNU,C,28,0|DELIV_PPP,C,6,0|G40T,C,2,0|G40,C,35,0|G405,N,2,0|TOV_SIGN2,C,1,0|CREATEDATE,D,8,0|MODIFIED_D,D,8,0|ARM_ID,N,3,0|VERSION,C,4,0";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//method1(args);
		//method2(args);
		//method3(args);
		//method4(args);
		//method5(args);
		method6(args);
                method7(args);
	}
	
	public static void method5(String[] args) throws Exception {	
		Charset stringCharset = Charset.forName("Cp866");
		//File file = new File("data/215451/gds_im.dbf");
		File file = new File("data/215451/tir_im.dbf");
		net.iryndin.jdbf.reader.DbfReader reader = new net.iryndin.jdbf.reader.DbfReader(file);
		DbfMetadata meta = reader.getMetadata();
		System.out.println(meta);
		net.iryndin.jdbf.core.DbfRecord rec = null;
		List<net.iryndin.jdbf.core.DbfRecord> recs = new ArrayList<net.iryndin.jdbf.core.DbfRecord>(10);
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		while ((rec = reader.read()) != null) {
			rec.setStringCharset(stringCharset);
			String s = rec.getString("G022");
			System.out.println(rec.getStringRepresentation());
			
			Map<String,Object> map = rec.toMap();
			maps.add(map);
			System.out.println(map);
			recs.add(rec);			
		}
		reader.close();
		
		String fieldsInfo = "KONTR,C,1,0|N_MDP,C,8,0|MDPLETTERS,C,2,0|W_LIST_NO,N,2,0|CUST_REG_N,C,6,0|CUSTOMPOST,C,8,0|DESIGN_PPP,C,6,0|G542,D,8,0|REG_TIME,C,5,0|SPECIF_GDS,N,3,0|G05_NTF,N,3,0|G222,N,16,2|G35_NTF,N,17,2|G531,C,8,0|GA3,C,8,0|GA1,C,100,0|A_NLIC,C,12,0|A_DLIC,D,8,0|G022,C,76,0|G023,C,150,0|G15C,C,3,0|G15C_ABC2,C,2,0|G501,C,52,0|G502,C,150,0|CNTRYDRIVE,C,3,0|DRIVE_ABC2,C,2,0|DRIVERTYPE,N,1,0|DRIVEROGRN,C,15,0|INN_DRIVER,C,20,0|DRIVERITN,C,13,0|DRIVER_FIO,C,60,0|DRIVERPASS,C,30,0|HOLDERIDN,C,18,0|FAGENTNAME,C,76,0|FAGENTADDR,C,76,0|ITN_RECEIV,C,15,0|G082,C,76,0|G083,C,150,0|OGRNR,C,15,0|INNR,C,12,0|KPP_RECEIV,C,9,0|ITNR,C,13,0|ONLYONEGDS,L,1,0|G05_NTFR,N,3,0|G222_R,N,16,2|G35_NTFR,N,17,2|CUSTCODES,C,8,0|G082S,C,76,0|G083S,C,150,0|GINNS,C,12,0|GKPPS,C,9,0|ONLYONEGD2,L,1,0|G05_NTFS,N,3,0|G222_S,N,16,2|G35_NTFS,N,17,2|CUSTCODET,C,8,0|G082T,C,76,0|G083T,C,150,0|GINNT,C,12,0|GKPPT,C,9,0|ONLYONEGD3,L,1,0|G05_NTFT,N,3,0|G222_T,N,16,2|G35_NTFT,N,17,2|G26,C,2,0|N_TD,C,250,0|N_TR,C,12,0|VIN,C,20,0|TC_COUNTRY,C,3,0|TC_ABC2,C,2,0|N_TRAILER,C,17,0|N_TRAILER2,C,17,0|TC_VOLUME,N,3,0|G3161,C,75,0|G52CODE,C,2,0|G447C,C,26,0|G52DATE,D,8,0|G504,D,8,0|SMELLED,C,23,0|SMELLEDATE,D,8,0|CODELNP_O,C,8,0|NUM_LNP_O,C,4,0|FOREIGN_PL,L,1,0|NUM_PLOM,C,15,0|ONCE_ADM,N,1,0|CMP_PGTD,C,1,0|NLIST,N,3,0|N_DOK_OUT,C,20,0|N_DOK_IN,C,20,0|D_IN_OKDT,D,8,0|PRIM,C,100,0|DAT_PD1,D,8,0|TIME_OUT1,C,5,0|PIGEONFIL1,C,12,0|TOTAL_NUV,N,2,0|DAT_PD,D,8,0|TIME_OUT,C,5,0|PIGEONFILE,C,13,0|GOTIT,C,1,0|GTK_FNAME,C,12,0|ISREAD,C,1,0|RTUN_FNAME,C,12,0|RTUN_READ,C,1,0|DATE_RCBD,D,8,0|SIGN2,C,1,0|RCP_CHKSUM,C,30,0|RTUO_FNAME,C,12,0|RTUO_READ,C,1,0|CREATEDATE,D,8,0|MODIFIED_D,D,8,0|ONTHEPAPER,D,8,0|STATUS_DOC,C,1,0|G05,N,3,0|AVTS_DOCNN,C,32,0|AVTS_RES_N,C,1,0|AVTS_DOCNC,C,32,0|AVTS_RES_C,C,1,0";
		DbfMetadata meta1 = DbfMetadataUtils.fromFieldsString(fieldsInfo);
		meta1.setRecordsQty(maps.size());
		//meta1.setRecordsQty(1);
		FileOutputStream out = new FileOutputStream("2.dbf"); 
		net.iryndin.jdbf.writer.DbfWriter writer = new net.iryndin.jdbf.writer.DbfWriter(meta1,out);
		writer.setStringCharset("Cp866");
//		for (net.iryndin.jdbf.core.DbfRecord r : recs) {
//			writer.write(rec);
//		}
		
		for (Map<String,Object> map : maps) {
			writer.write(map);
		}
		
		writer.close();
		
//		String fieldsInfo = "KONTR,C,1,0|N_MDP,C,8,0|MDPLETTERS,C,2,0|W_LIST_NO,N,2,0|CUST_REG_N,C,6,0|CUSTOMPOST,C,8,0|DESIGN_PPP,C,6,0|G542,D,8,0|REG_TIME,C,5,0|SPECIF_GDS,N,3,0|G05_NTF,N,3,0|G222,N,16,2|G35_NTF,N,17,2|G531,C,8,0|GA3,C,8,0|GA1,C,100,0|A_NLIC,C,12,0|A_DLIC,D,8,0|G022,C,76,0|G023,C,150,0|G15C,C,3,0|G15C_ABC2,C,2,0|G501,C,52,0|G502,C,150,0|CNTRYDRIVE,C,3,0|DRIVE_ABC2,C,2,0|DRIVERTYPE,N,1,0|DRIVEROGRN,C,15,0|INN_DRIVER,C,20,0|DRIVERITN,C,13,0|DRIVER_FIO,C,60,0|DRIVERPASS,C,30,0|HOLDERIDN,C,18,0|FAGENTNAME,C,76,0|FAGENTADDR,C,76,0|ITN_RECEIV,C,15,0|G082,C,76,0|G083,C,150,0|OGRNR,C,15,0|INNR,C,12,0|KPP_RECEIV,C,9,0|ITNR,C,13,0|ONLYONEGDS,L,1,0|G05_NTFR,N,3,0|G222_R,N,16,2|G35_NTFR,N,17,2|CUSTCODES,C,8,0|G082S,C,76,0|G083S,C,150,0|GINNS,C,12,0|GKPPS,C,9,0|ONLYONEGD2,L,1,0|G05_NTFS,N,3,0|G222_S,N,16,2|G35_NTFS,N,17,2|CUSTCODET,C,8,0|G082T,C,76,0|G083T,C,150,0|GINNT,C,12,0|GKPPT,C,9,0|ONLYONEGD3,L,1,0|G05_NTFT,N,3,0|G222_T,N,16,2|G35_NTFT,N,17,2|G26,C,2,0|N_TD,C,250,0|N_TR,C,12,0|VIN,C,20,0|TC_COUNTRY,C,3,0|TC_ABC2,C,2,0|N_TRAILER,C,17,0|N_TRAILER2,C,17,0|TC_VOLUME,N,3,0|G3161,C,75,0|G52CODE,C,2,0|G447C,C,26,0|G52DATE,D,8,0|G504,D,8,0|SMELLED,C,23,0|SMELLEDATE,D,8,0|CODELNP_O,C,8,0|NUM_LNP_O,C,4,0|FOREIGN_PL,L,1,0|NUM_PLOM,C,15,0|ONCE_ADM,N,1,0|CMP_PGTD,C,1,0|NLIST,N,3,0|N_DOK_OUT,C,20,0|N_DOK_IN,C,20,0|D_IN_OKDT,D,8,0|PRIM,C,100,0|DAT_PD1,D,8,0|TIME_OUT1,C,5,0|PIGEONFIL1,C,12,0|TOTAL_NUV,N,2,0|DAT_PD,D,8,0|TIME_OUT,C,5,0|PIGEONFILE,C,13,0|GOTIT,C,1,0|GTK_FNAME,C,12,0|ISREAD,C,1,0|RTUN_FNAME,C,12,0|RTUN_READ,C,1,0|DATE_RCBD,D,8,0|SIGN2,C,1,0|RCP_CHKSUM,C,30,0|RTUO_FNAME,C,12,0|RTUO_READ,C,1,0|CREATEDATE,D,8,0|MODIFIED_D,D,8,0|ONTHEPAPER,D,8,0|STATUS_DOC,C,1,0|G05,N,3,0|AVTS_DOCNN,C,32,0|AVTS_RES_N,C,1,0|AVTS_DOCNC,C,32,0|AVTS_RES_C,C,1,0";
//		
//		DbfMetadata meta = DbfMetadataUtils.fromFieldsString(fieldsInfo);
//		System.out.println(meta);
		
		
	}
	
	public static void method6(String[] args) throws Exception {
		//File file = new File("data/215451/tir_im.dbf");
		//File file = new File("data/215451/gds_im.dbf");
		
		File file = new File("data/218864/tir_im.dbf");
		//File file = new File("data/218864/gds_im.dbf");
		testReadWriteReadFile(file);
	}
        
        /**
         * Example: Write to DBF
         */
        public static void method7(String[] args) throws FileNotFoundException, IOException, ParseException {
            // Set metadata
            String fieldsInfo = "KONTR,C,1,0|N_MDP,C,8,0";
            DbfMetadata meta = DbfMetadataUtils.fromFieldsString(fieldsInfo);
            meta.setType(DbfFileTypeEnum.dBASEIV3);
            
            // Create output stream and DBF file
            FileOutputStream out = new FileOutputStream("1.dbf");
            DbfWriter writer = new DbfWriter(meta,out);
            writer.setStringCharset("Cp866");
            
            // Write to file
            List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("KONTR", "a");
            map.put("N_MDP", "2");
            writer.write(map);
            
            // The end
            writer.close();
        }
        
	static void testReadWriteReadFile(File file)  throws Exception  {
		Charset stringCharset = Charset.forName("Cp866");
		
		// 1. Read file
		
		DbfReader reader = new DbfReader(file);
		DbfMetadata meta = reader.getMetadata();
		DbfRecord rec = null;
		List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
		while ((rec = reader.read()) != null) {
			rec.setStringCharset(stringCharset);
			Map<String,Object> map = rec.toMap();
			maps.add(map);
		}
		reader.close();
		
		// 2. Write file
		String fieldsInfo = meta.getFieldsStringRepresentation();
		System.out.println(fieldsInfo);
		DbfMetadata meta1 = DbfMetadataUtils.fromFieldsString(fieldsInfo);
		meta1.setRecordsQty(maps.size());
		FileOutputStream out = new FileOutputStream("1.dbf"); 
		DbfWriter writer = new DbfWriter(meta1,out);
		writer.setStringCharset(stringCharset);
		for (Map<String,Object> map : maps) {
			writer.write(map);
		}
		writer.close();
		
		// 3. Read written file and compare data with initially read data
		{
			DbfReader reader1 = new DbfReader(new File("1.dbf"));
			DbfRecord rec1 = null;
			List<Map<String,Object>> maps2 = new ArrayList<Map<String,Object>>();
			while ((rec1 = reader1.read()) != null) {
				rec1.setStringCharset(stringCharset);
				Map<String,Object> map = rec1.toMap();
				maps2.add(map);
			}
			reader.close();
			
			// compare
			int i=0;
			boolean allEqual = true;
			for (Map<String,Object> map : maps2) {
				boolean mapEqual = JdbfUtils.compareMaps(map, maps.get(i));
				System.out.println(mapEqual);
				allEqual = allEqual && mapEqual;
				i++;
			}
			System.out.println("allEqual="+allEqual);
		}
	}
}
