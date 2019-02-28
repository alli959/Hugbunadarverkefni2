/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: search.generator
 */

package search.generator;

	 import javafx.util.Pair;
	 import org.json.JSONArray;
	 import org.json.JSONObject;
	 import org.json.JSONException;

	 import java.io.IOException;
	 import java.util.*;

/**
 d8888888888888888888888"
 888888888888888888PYP"'
 Bitchin factory                d88888888888888888D
 Homie                       8888888888888888P'     Thessi er med mikid af random methods
 Y8888888888888b       hann er btw abstract vegna thess ad madur
 C8888888Y888888P       tharf ad extenda svo hann er useful
 Y88888'd88888"
 8888P d8888P
 d8888D 88888
 d888P'  Y88dP
 nY88Pn    Y88            8"""-----....._____
 N   +N    88'            8                  NNNNNN8
 N   +N  nd88n            P                  NNNNNNP
 N   +N  N  +N           d  dNN   ...       dNNNNNN
 __...---"Nn.            N   +N  N  +N           8  NNP  dNNP  dNN  NNNNNNN
 8""         NNNNn          N   +N  N  +N           8       ""'   NNP  NNNNNNN
 8       oo  NNNNN          N   +N  N  +N           8                  NNNNNNP
 Y  dN   NN  NNNNN          N   +N  N  +N           P       ooo       dNNNNNN
 b NY   ""  YNNNN          N   +N  N  +N          d       dNN'  dNN  NNNNNNN
 8        _  bNNNb         N   +N  N  +N          8       """   NNP  NNNNNNN
 8  o8   88  NNNNN         N   +N  N  +N          8                  NNNNNNN
 Y  BP   ""  NNNNN         N   +N  N  +N          8            nnn   NNNNNNP
 b          NNNNN         N   +N  N  +N          P            NNP  dNNNNNN
 8          YNNNN         N   +N  M  +N         d             ""   NNNNNNN
 8           NNNNb        N   +N  N  +N         8                  NNNNNNN
 Y           NNNNN      __N___+N__N  +N         8                  NNNNNNP
 b          NNNNNooooodP""""""""YNNNNNNbcgmmnnn8                 dNNNNNN
 8          """'                         `"""""8                 NNNNNNN
 8                                             P                 NNNNNNN
 Y                          NNNNNNNNN         d                  NNNNNNN
 b                         NNNNNNNNN         8                  NNNNNNP
 8                         NNNNNNNNP         8                 dNNNNNN
 8        Lol              NNNNNNNN;         8                 NNNNNNN
 Y     o                   NNNNNNNN:         P                 NNNNNNN
 b   -|-                  NNNNNNNN;        d                  NNNNNNP
 ______8__........----------""""""""""""------...8..______         NNNNNNN
 _________........----------""""""""""""------......_____  """""----NNNNNNN
 """""----....___ """--
 """--- *
 */


public abstract class Factory<Ent extends Entity> {
	//need to initialize these before using the "getAll" and "save" methods
	protected String getAllURL = "getAllURL-not-initialized-properly-somehow";
	protected String updateURL = "updateAllURL-not-Initialized";
	protected String removeURL;
	protected String getOneURL;

	/*******************************************
	 *       REPO STYLE METHODS
	 *******************************************/

	protected abstract Ent jsonToEntity(JSONObject json);
	public abstract Ent generate();
	public boolean remove(Long id) throws IOException {
		Pair<String, String> pair = new Pair<>("id", "" + id);
		Request r = new Request(removeURL, pair);
		r.resolve();
		return true;
	}

	//returns the id
	public Long save(Ent ent) throws IOException {
		JSONObject o = new JSONObject();
		List<Pair<String, String>> params = ent.getParameters();
		Request r = new Request(updateURL, ent.getParameters());
		JSONArray a = r.resolve();
		Long l = Long.parseLong(a.get(0).toString());
		return l;
	}

	public Long getAsLong(String key, JSONObject o) {
		Long l;
		try{ l = Long.parseLong((String) o.get(key)); }
		catch(ClassCastException e) {
			try { l = new Long((Integer) o.get(key)); }
			catch(ClassCastException f) {
				l = (Long) o.get(key);
			}
		}
		return l;
	}

	public Map<Integer, String> getStringMapFromJSON(JSONObject o) {
		Map<Integer, String> map = new HashMap<>();
		Integer i = 0;
		while(true) {
			try { map.put(i, (String) o.get(i.toString()).toString()); i++; }
			catch(JSONException e) { return map; }
		}
	}

	public Map<Integer, Long> getLongMapFromJSON(JSONObject o) {
		Map<Integer, String> stringMap = getStringMapFromJSON(o);
		Map<Integer, Long> longMap = new HashMap<>();
		for(Map.Entry<Integer, String> e : stringMap.entrySet())
			longMap.put(e.getKey(), Long.parseLong(e.getValue()));
		return longMap;
	}

	public List getOne(Long id) {
		Pair<String, String> pair = new Pair("id", "" + id);
		JSONArray json;
		try {
			Request r = new Request(getOneURL, pair);
			json = r.resolve();
		} catch(IOException e) {
			e.printStackTrace(System.out);
			return new ArrayList<>();
		}


		List<Ent> entity = new ArrayList<>();
		entity.add(jsonToEntity((JSONObject) json.get(0)));
		return entity;
	}

	public Ent one(Long id) throws IOException {
		List<Ent> list = getOne(id);
		return list.get(0);
	}

	public List getAll() throws IOException {
		Request r = new Request(getAllURL);
		JSONArray json =  r.resolve();
		String s = json.toString();
		List<Ent> entities = new ArrayList<>();

		for(int i = 0; i < json.length(); i++)
			entities.add(jsonToEntity((JSONObject) json.get(i)));
		return entities;
	}

	/*******************************************
	 *       RANDOM STYLE METHODS
	 *******************************************/
	private static final int DAY_IN_MS = 86400000;

	private static Map<Integer, String> arrayListToMap(ArrayList<String> arrayList) {
		Map<Integer, String> map = new HashMap<>();
		for(int i = 0; i < arrayList.size(); i++)
			map.put(i, arrayList.get(i));
		return map;
	}

	private static List<String> getRandomList(String[] array) {
		List<String> list = new ArrayList<>(Arrays.asList(array));
		Collections.shuffle(list);
		int a = list.size() - 1;
		int randomAmount = (int) (a*Math.random());
		List<String> l = list.subList(0, randomAmount + 1);
		return l;
	}

	public static Map<Integer, String> getRandomMap(String[] array) {
		ArrayList<String> list = new ArrayList<>(getRandomList(array));
		return arrayListToMap(list);
	}

	public static String getRandom(String[] array) {
		int a = (int) (Math.random() * array.length);
		return array[a];
	}

	public static boolean randomBoolean()                      { return Math.random() > 0.5; }
	public static double  randomDouble(double seed)            { return Math.random() * seed; }
	public static double  randomDouble(double seed, double width) { return seed + Math.random() * width; }
	public static int     randomInt(int seed)                  { return (int) (Math.random() * seed); }
	public static Long    randomDate(Long seed, int width) {
		Date date = new Date(seed);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.getTime().getTime();
	}

	//for long text like reviews we put them into arraylist :)
	public static List<String> splitStringToArrayList(String string) {
		List<String> list = new ArrayList<>();
		String[] split = string.split("");
		String char100 = "";
		for(String c : split) {
			char100 += c;
			if(char100.length() == 20) {
				list.add(char100);
				char100 = "";
			}
		}
		list.add(char100);
		return list;
	}

	public static String randomCC() {
		String cc = "";
		for(int i = 0; i < 23; i++) {
			cc += Integer.toString(randomInt(10));
		}
		return cc;
	}

	public static Map<Long, Integer> getRandomAvailability() {
		Map<Long, Integer> map = new HashMap<>();
		Long today = randomDate(new Date().getTime(), 0);
		for(int i = 0; i < 400; i++)
		map.put(today + i * DAY_IN_MS, randomInt(30));
		return map;
	}

	public static Pair<Long, Long> randomTimeInterval() {
		Long first = randomDate(new Date().getTime(), randomInt(200));
		Long second = randomDate(first, 30);
		return new Pair(first, second);
	}

	/*******************************************
	*       DATA FOR RANDOMIZATION
	*******************************************/
	public static final String[] roomType = {
		"sgl",
		"dbl",
		"twin",
		"something-else"
	};

	public static final String[] amenities = {
		"wifi",
		"shower",
		"breakfast",
		"private_bathroom",
		"tea",
		"coffee",
		"swimming_pool",
		"accessible",
		"linen"
	};

	public static final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur ex nunc, ullamcorper et euismod nec, pretium quis lectus. Nullam a facilisis leo. Phasellus egestas turpis a purus malesuada tempus. Curabitur a neque et lorem venenatis mattis vitae ut ex. Nam faucibus congue arcu, id interdum est sodales sit amet. Curabitur in placerat lacus, non malesuada dolor. Morbi est nibh, condimentum et orci sit amet, aliquet dapibus dolor. Proin tincidunt justo vel fermentum dictum. Donec at nunc nec purus semper pharetra nec non justo. Vivamus nibh leo, malesuada cursus ipsum eget, viverra fringilla libero. Morbi vel pretium nisl. Ut viverra molestie dolor, ut blandit nunc pretium a. Integer a euismod lacus. Integer fermentum lacus ante, ut porttitor velit commodo id. Etiam feugiat odio ac elit ullamcorper sodales. Fusce auctor varius feugiat."	;

	public static final String[] subject = {
		"You're an awesome friend.",
		"You're a gift to those around you.",
		"You're a smart cookie.",
		"You are awesome!",
		"You have impeccable manners.",
		"I like your style.",
		"You have the best laugh.",
		"I appreciate you.",
		"You are the most perfect you there is.",
		"You are enough.",
		"You're strong.",
		"Your perspective is refreshing.",
		"I'm grateful to know you.",
		"You light up the room.",
		"You deserve a hug right now.",
		"You should be proud of yourself.",
		"You're more helpful than you realize.",
		"You have a great sense of humor.",
		"You've got an awesome sense of humor!",
		"You are really courageous.",
		"Your kindness is a balm to all who encounter it.",
		"You're all that and a super-size bag of chips.",
		"On a scale from 1 to 10, you're an 11.",
		"You are strong.",
		"You're even more beautiful on the inside than you are on the outside.",
		"You have the courage of your convictions.",
		"I'm inspired by you.",
		"You're like a ray of sunshine on a really dreary day.",
		"You are making a difference.",
		"Thank you for being there for me.",
		"You bring out the best in other people."
	};

	public static final String[] imageUrl = {
		"https://i.imgur.com/6c76Tdn.jpg",
		"https://i.imgur.com/LFgN8Pd.jpg",
		"https://i.imgur.com/LDhMXCB.jpg",
		"https://i.imgur.com/ojjACfZ.jpg",
		"https://i.imgur.com/qLyomx3.jpg",
		"https://i.imgur.com/btJFPOL.jpg",
		"https://i.imgur.com/wbhxWSo.jpg",
		"https://i.imgur.com/TvLUBJp.jpg",
		"https://i.imgur.com/dS4WILC.jpg",
		"https://i.imgur.com/TJoqdrp.jpg",
		"https://i.imgur.com/7mwzU6P.jpg",
		"https://i.imgur.com/qUBiJNr.jpg",
		"https://i.imgur.com/rYgxnbE.jpg",
		"https://i.imgur.com/yFiLDSq.jpg",
		"https://i.imgur.com/3CU2kb0.jpg",
		"https://i.imgur.com/c7fkAPd.jpg",
		"https://i.imgur.com/rQxaSum.jpg",
		"https://i.imgur.com/R85lN5a.jpg",
		"https://i.imgur.com/3LTxbQj.jpg",
		"https://i.imgur.com/EjZ75rt.jpg"
	};

	public static final String[] humanName = {
		"Bernard_Hunt",
		"Jaime_Holloway",
		"Alfonso_Carpenter",
		"Clinton_Bowman",
		"Darryl_Cobb",
		"Tammy_Holland",
		"Erica_Zimmerman",
		"Jeannie_Conner",
		"Ethel_Hanson",
		"Antoinette_Joseph",
		"Elsie_Hubbard",
		"Terry_Pena",
		"Sonja_Carter",
		"Charles_Hughes",
		"James_Marshall",
		"Ernest_Jimenez",
		"Rebecca_Barber",
		"Alice_Perez",
		"Jenny_Owens",
		"Mercedes_Bradley",
		"Misty_Kelley",
		"Russell_Copeland",
		"Kendra_Waters",
		"Rene_Alvarado",
		"Geraldine_West",
		"Van_Perry",
		"Ricky_Perkins",
		"Loren_Moss",
		"Lorenzo_Massey",
		"Johnathan_Lewis",
		"Rafael_Washington",
		"Josephine_Carroll",
		"Alberta_Swanson",
		"Ruth_Dunn",
		"Clay_Warren",
		"Joyce_Hudson",
		"Nichole_Howell",
		"Rhonda_Gibson",
		"Annie_Clayton",
		"Courtney_Moore",
		"Elena_Jones",
		"Floyd_Page",
		"Jackie_Lloyd",
		"Homer_Fletcher",
		"Rachel_Rivera",
		"Lloyd_Cortez",
		"Jeannette_Matthews",
		"Leo_Payne",
		"Barbara_Phelps",
		"Marc_Hall"
	};

	public static final String[] hotelName = {
		"Wet_Spring_Hotel",
		"Bree_Wonder",
		"Hotel_Hotel",
		"Hotel_Fighters",
		"Hotel_Red_Day",
		"You're_Going_To_Want_To_Eat_this_Hotel",
		"Between_the_Hotels_and_Me",
		"Megahotel",
		"Hotel_Dream_and_a_Pinch_of_Salt",
		"My_Wet_Romance",
		"Brians_Wet_Hotel",
		"No_Hotel,_Only_Steak",
		"Of_Men_and_Humming_Birds",
		"Saving_Bree",
		"School_of_the_One_Zillion_Humming_Birds",
		"They_Might_Be_Wet_Humming_Birds",
		"One_Zillion_Humming_Birds",
		"Hotel_Hotel_Cult",
		"The_Reykjavik_Strippers",
		"Hotel_Kid_Bree",
		"Reykjavik_Thunder_Shoulders",
		"Hotel_One_Zillion",
		"One_Zillion_Snuggling_Iced_Humming_Birds",
		"Snuggling_at_the_Disco",
		"One_Zillion_Odd_Humming_Birds",
		"Hotel_Failure",
		"Wet_Humming_Birds_Dream",
		"This_Hotel",
		"Its_My_Hotel",
		"Flight_of_the_Hotels",
		"Snuggling_in_Reykjavik",
		"Hotelplay",
		"The_Hotel_Can_For_the_Street_Artists",
		"The_Wet_Street_Artistss_Club",
		"Hotel_Hotel",
		"Hotel_Fighters",
		"Hotel_Red_Day",
		"Hotels_and_a_Hotel",
		"Bree_Eats_the_Hotel",
		"Hotel_Dream_and_a_Pinch_of_Hotel",
		"My_Wet_Romance",
		"Brees_Wet_Hotel",
		"No_Hotel",
		"Reykjavik_Panic",
		"Hotels_for_the_Street_Artists",
		"Saving_Bree",
		"One_Zillion_Humming_Birds",
		"The_Snuggling_Hotel_Street_Artists",
		"Hotel_Attack",
		"Hotel_for_the_Street_Artists",
		"Hotelback",
		"The_Hotel_Bree_Project",
		"Snuggling_Hotels",
		"Puddle_of_Hotel",
		"Disciples_Of_Reykjavik",
		"The_Hotel_B_Experience",
		"Snuggling_at_the_Disco",
		"Hotel_Army",
		"Hotel_Tribute",
		"This_Hotel",
		"Its_My_Hotel",
		"Allo_Hotels"
	};

	public static final String[] email = {
		"amcnysche9k@webmd.com",
		"sskilbeck9l@last.fm",
		"zilchenko9m@ebay.com",
		"jblanche9n@webmd.com",
		"bliebrecht9o@newsvine.com",
		"clejeune9p@smh.com.au",
		"rsilverthorn9q@sciencedirect.com",
		"kburgess9r@tinypic.com",
		"rmcconnal9s@cmu.edu",
		"vbellinger9t@webnode.com",
		"amcgrale9u@xinhuanet.com",
		"cbladder9v@gravatar.com",
		"aearland9w@tamu.edu",
		"jkier9x@imageshack.us",
		"rchittey9y@bigcartel.com",
		"mgreatbach9z@storify.com",
		"mbenfielda0@accuweather.com",
		"avuitteta1@va.gov",
		"ldadea2@networkadvertising.org",
		"lkelsalla3@is.gd",
		"twoodyearea4@microsoft.com",
		"cdominguesa5@hp.com",
		"wraimenta6@sbwire.com",
		"lconichiea7@latimes.com",
		"kstiegera8@apple.com",
		"moharea9@storify.com",
		"mkidsleyaa@shareasale.com",
		"ldarkerab@wordpress.com",
		"tkimberleyac@bing.com",
		"aebentallad@last.fm",
		"gellitae@telegraph.co.uk",
		"kburleyaf@51.la",
		"nhotsonag@theglobeandmail.com",
		"dgoldringah@phoca.cz",
		"adienesai@bloglines.com",
		"jreynaaj@feedburner.com",
		"cmanthaak@intel.com",
		"pbridgemanal@whitehouse.gov",
		"wpackingtonam@about.me",
		"nbainsan@bandcamp.com",
		"rklimkowskiao@1und1.de",
		"nrozierap@oracle.com",
		"ebullockeaq@springer.com",
		"ehoviear@nifty.com",
		"ssalleryas@youtube.com",
		"inickollat@hhs.gov",
		"hpattersau@yellowpages.com",
		"ghawyesav@usatoday.com",
		"mdemcakaw@moonfruit.com",
		"mdemschkeax@yandex.ru",
		"cmolyneuxay@hao123.com",
		"pgrethamaz@t.co",
		"echaudronb0@umn.edu",
		"dtabardb1@liveinternet.ru",
		"earentsb2@dagondesign.com",
		"lcockerellb3@wufoo.com",
		"kdavanab4@vimeo.com",
		"rphilcoxb5@admin.ch",
		"hstoppardb6@cbslocal.com",
		"vdragoeb7@bloglines.com",
		"whullettb8@ustream.tv",
		"mebbingsb9@seesaa.net",
		"fhebblewhiteba@bravesites.com",
		"dmiebesbb@squidoo.com",
		"pboheybc@bloomberg.com",
		"gmillwardbd@theatlantic.com",
		"csheermanbe@wikispaces.com",
		"dtrebilcockbf@gravatar.com",
		"rjearumbg@e-recht24.de",
		"olorekbh@tumblr.com",
		"cmeneghibi@amazon.de",
		"binesonbj@oracle.com",
		"akobierzyckibk@opensource.org"
	};
}
