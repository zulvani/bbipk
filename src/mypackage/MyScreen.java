package mypackage;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public final class MyScreen extends MainScreen {
	
	private static long KEY = 0x2c99fe0a03117957L;
	private PersistentObject persObject;
	
	private XYEdges edges = new XYEdges(1, 1, 1, 1); 
	private XYEdges innerColor = new XYEdges(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK); 
	private XYEdges outerColor = new XYEdges(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK); 
	private Border border = BorderFactory.createBevelBorder(edges, outerColor, innerColor);
	
	private EditField nilaiMatematika;
	private EditField nilaiPemograman;
	private EditField nilaiDatabase;
	private EditField nilaiRpl;
	
	private EditField sksMatematika;
	private EditField sksPemograman;
	private EditField sksDatabase;
	private EditField sksRpl;
	
	private EditField mutuMatematika = new MyEditField(EditField.READONLY);
	private EditField mutuPemograman = new MyEditField(EditField.READONLY);
	private EditField mutuDatabase = new MyEditField(EditField.READONLY);
	private EditField mutuRpl = new MyEditField(EditField.READONLY);
	
	private ButtonField btnSave = new ButtonField("Simpan", ButtonField.CONSUME_CLICK);
	
	public MyScreen() {
		setTitle("Hitung IPK");
		
		persObject = PersistentStore.getPersistentObject(KEY);
		
		if(persObject.getContents() == null)
			createInputUI();
		else
			createResultUI();
		
	}
	
	private void createInputUI(){
		nilaiMatematika = new MyEditField();
		nilaiMatematika.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				mutuMatematika.setText(String.valueOf(getNilaiMutu(nilaiMatematika.getText())));
			}
		});
		
		nilaiPemograman = new MyEditField();
		nilaiPemograman.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				mutuPemograman.setText(String.valueOf(getNilaiMutu(nilaiPemograman.getText())));
			}
		});
		
		nilaiDatabase = new MyEditField();
		nilaiDatabase.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				mutuDatabase.setText(String.valueOf(getNilaiMutu(nilaiDatabase.getText())));
			}
		});
		
		nilaiRpl = new MyEditField();
		nilaiRpl.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				mutuRpl.setText(String.valueOf(getNilaiMutu(nilaiRpl.getText())));
			}
		});
		
		nilaiMatematika.setBorder(border);
		nilaiPemograman.setBorder(border);
		nilaiDatabase.setBorder(border);
		nilaiRpl.setBorder(border);
		
		sksMatematika = new MyEditField(EditField.FILTER_INTEGER);
		sksPemograman = new MyEditField(EditField.FILTER_INTEGER);
		sksDatabase = new MyEditField(EditField.FILTER_INTEGER);
		sksRpl = new MyEditField(EditField.FILTER_INTEGER);
		
		mutuMatematika.setBorder(border);
		mutuPemograman.setBorder(border);
		mutuDatabase.setBorder(border);
		mutuRpl.setBorder(border);
		
		sksMatematika.setBorder(border);
		sksPemograman.setBorder(border);
		sksDatabase.setBorder(border);
		sksRpl.setBorder(border);
		
		VerticalFieldManager vfm = new VerticalFieldManager();
		
		HorizontalFieldManager hfmHead = new HorizontalFieldManager();
		hfmHead.add(new MyLabelField("Matakuliah"));
		hfmHead.add(new MyLabelFieldFix("Nilai"));
		hfmHead.add(new MyLabelFieldFix("SKS"));
		hfmHead.add(new MyLabelFieldFix("Mutu"));
		
		HorizontalFieldManager hfmMatematika = new HorizontalFieldManager(
				Manager.HORIZONTAL_SCROLL | Manager.USE_ALL_WIDTH);
		hfmMatematika.add(new MyLabelField("Matematika"));
		hfmMatematika.add(nilaiMatematika);
		hfmMatematika.add(sksMatematika);
		hfmMatematika.add(mutuMatematika);
		
		HorizontalFieldManager hfmPemograman = new HorizontalFieldManager();
		hfmPemograman.add(new MyLabelField("Pemograman"));
		hfmPemograman.add(nilaiPemograman);
		hfmPemograman.add(sksPemograman);
		hfmPemograman.add(mutuPemograman);
		
		HorizontalFieldManager hfmDatabase = new HorizontalFieldManager();
		hfmDatabase.add(new MyLabelField("Database"));
		hfmDatabase.add(nilaiDatabase);
		hfmDatabase.add(sksDatabase);
		hfmDatabase.add(mutuDatabase);
		
		HorizontalFieldManager hfmRpl = new HorizontalFieldManager();
		hfmRpl.add(new MyLabelField("RPL"));
		hfmRpl.add(nilaiRpl);
		hfmRpl.add(sksRpl);
		hfmRpl.add(mutuRpl);
		
		btnSave.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				saveData();
			}
		});
		
		vfm.add(hfmHead);
		vfm.add(hfmMatematika);
		vfm.add(hfmPemograman);
		vfm.add(hfmDatabase);
		vfm.add(hfmRpl);
		vfm.add(btnSave);
		
		add(vfm);
	}
	
	private void createResultUI(){
		persObject = PersistentStore.getPersistentObject(KEY);
		Hashtable ht = (Hashtable) persObject.getContents();
		
		if(ht != null){
			VerticalFieldManager vfm = new VerticalFieldManager();
			
			HorizontalFieldManager hfmHead = new HorizontalFieldManager();
			hfmHead.add(new MyLabelField("Matakuliah"));
			hfmHead.add(new MyLabelFieldFix("Nilai"));
			hfmHead.add(new MyLabelFieldFix("SKS"));
			hfmHead.add(new MyLabelFieldFix("Mutu"));
			
			HorizontalFieldManager hfmMatematika = new HorizontalFieldManager();
			hfmMatematika.add(new MyLabelField("Matakuliah"));
			hfmMatematika.add(new MyLabelFieldFix(ht.get("nilaiMatematika")));
			hfmMatematika.add(new MyLabelFieldFix(ht.get("sksMatematika")));
			hfmMatematika.add(new MyLabelFieldFix(String.valueOf(getNilaiMutu((String) ht.get("nilaiMatematika")))));
			
			HorizontalFieldManager hfmPemograman = new HorizontalFieldManager();
			hfmPemograman.add(new MyLabelField("Pemograman"));
			hfmPemograman.add(new MyLabelFieldFix(ht.get("nilaiPemograman")));
			hfmPemograman.add(new MyLabelFieldFix(ht.get("sksPemograman")));
			hfmPemograman.add(new MyLabelFieldFix(String.valueOf(getNilaiMutu((String) ht.get("nilaiPemograman")))));
			
			HorizontalFieldManager hfmDatabase = new HorizontalFieldManager();
			hfmDatabase.add(new MyLabelField("Database"));
			hfmDatabase.add(new MyLabelFieldFix(ht.get("nilaiDatabase")));
			hfmDatabase.add(new MyLabelFieldFix(ht.get("sksDatabase")));
			hfmDatabase.add(new MyLabelFieldFix(String.valueOf(getNilaiMutu((String) ht.get("nilaiDatabase")))));
			
			HorizontalFieldManager hfmRpl = new HorizontalFieldManager();
			hfmRpl.add(new MyLabelField("RPL"));
			hfmRpl.add(new MyLabelFieldFix(ht.get("nilaiRpl")));
			hfmRpl.add(new MyLabelFieldFix(ht.get("sksRpl")));
			hfmRpl.add(new MyLabelFieldFix(String.valueOf(getNilaiMutu((String) ht.get("nilaiRpl")))));
			
			double tSksMutu = 
				(Double.parseDouble((String) ht.get("sksMatematika")) * getNilaiMutu((String) ht.get("nilaiMatematika"))) +
				(Double.parseDouble((String) ht.get("sksPemograman")) * getNilaiMutu((String) ht.get("nilaiPemograman"))) +
				(Double.parseDouble((String) ht.get("sksDatabase")) * getNilaiMutu((String) ht.get("nilaiDatabase"))) +
				(Double.parseDouble((String) ht.get("sksRpl")) * getNilaiMutu((String) ht.get("nilaiRpl")));
			double tSks = 
				Double.parseDouble((String) ht.get("sksMatematika")) +
				Double.parseDouble((String) ht.get("sksPemograman")) +
				Double.parseDouble((String) ht.get("sksDatabase")) +
				Double.parseDouble((String) ht.get("sksRpl"));
			double ipk = tSksMutu / tSks;			
			
			ButtonField btnDelete = new ButtonField("Hapus", ButtonField.CONSUME_CLICK);
			btnDelete.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					deleteData();
				}
			});
			
			vfm.add(hfmHead);
			vfm.add(hfmMatematika);
			vfm.add(hfmPemograman);
			vfm.add(hfmDatabase);
			vfm.add(hfmRpl);
			vfm.add(new LabelField("IPK = " + ipk));
			vfm.add(btnDelete);
			
			add(vfm);
		}
		else{
			createInputUI();
		}
	}
	
	private double getNilaiMutu(String nilai){
		double mutu = 0.0;
		if(nilai != null){
			if(nilai.equalsIgnoreCase("a")) mutu = 4.0;
			else if(nilai.equalsIgnoreCase("a-")) mutu = 3.7;
			else if(nilai.equalsIgnoreCase("b")) mutu = 3.0;
			else if(nilai.equalsIgnoreCase("b+")) mutu = 3.3;
			else if(nilai.equalsIgnoreCase("b-")) mutu = 2.7;
			else if(nilai.equalsIgnoreCase("c")) mutu = 2.0;
			else if(nilai.equalsIgnoreCase("c+")) mutu = 2.3;
			else if(nilai.equalsIgnoreCase("d")) mutu = 1.0;
			else if(nilai.equalsIgnoreCase("e")) mutu = 0.0;
		}
		return mutu;
	}
	
	private void saveData(){
		persObject = PersistentStore.getPersistentObject(KEY);
		Hashtable ht = new Hashtable();
		
		ht.put("nilaiMatematika", nilaiMatematika.getText());
		ht.put("nilaiPemograman", nilaiPemograman.getText());
		ht.put("nilaiDatabase", nilaiDatabase.getText());
		ht.put("nilaiRpl", nilaiRpl.getText());
		
		ht.put("sksMatematika", sksMatematika.getText());
		ht.put("sksPemograman", sksPemograman.getText());
		ht.put("sksDatabase", sksDatabase.getText());
		ht.put("sksRpl", sksRpl.getText());
		
		persObject.setContents(ht);
		persObject.commit();
		
		Dialog.inform("Berhasil disimpan, silahkan tutup aplikasi dan buka kembali!");
	}
	
	private void deleteData(){
		persObject = PersistentStore.getPersistentObject(KEY);
		persObject.setContents(null);
		persObject.commit();
		
		Dialog.inform("Berhasil dihapus, silahkan tutup aplikasi dan buka kembali!");
	}
	
	private class MyLabelField extends LabelField{
		public MyLabelField(Object label){
			super(label);
		}
		
		public int getPreferredWidth() {
			return 150;
		}
		
		public int getPreferredHeight() {
			return 25;
		}

		protected void layout(int arg0, int arg1) {
			super.layout(getPreferredWidth(), getPreferredHeight());
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}
	
	private class MyEditField extends EditField{
		public MyEditField(){
			
		}
		
		public MyEditField(long style){
			super(style);
		}

		public int getPreferredWidth() {
			return 50;
		}
		
		public int getPreferredHeight() {
			return 25;
		}

		protected void layout(int arg0, int arg1) {
			super.layout(getPreferredWidth(), getPreferredHeight());
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
		
	}
	
	private class MyLabelFieldFix extends LabelField{
		public MyLabelFieldFix(Object label){
			super(label);
		}
		
		public int getPreferredWidth() {
			return 50;
		}
		
		public int getPreferredHeight() {
			return 25;
		}
		
		protected void layout(int arg0, int arg1) {
			super.layout(getPreferredWidth(), getPreferredHeight());
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
		
	}
	
}
