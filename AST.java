package mine;

import java.util.Vector;
import java.util.Stack;
import java.io.*;
public class AST 
{
	public interface Node
	{
		<T> T accept (Visitor<T> v); 
	}
	public interface Visitor<T>
	{
		T visit (Loop loop);
		T visit (Condition condition);
		T visit (Conditional conditional);
		T visit (Identifier identifier);
		T visit (Location location);
		T visit (Structure s);
		T visit (Program p);
		T visit (Struct_def s);
		T visit (Place p);
		T visit (Remove r);
		T visit (Use u);
	}
	public static class Program implements Node
	{
		Vector<Structure> structs;
		public Program()
		{
			structs = new Vector<Structure>();
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Structure implements Node
	{
		Struct_def def;
		Vector<Statement> statements;
		public Structure(Struct_def d)
		{
		 def = d;
		 statements = new Vector<Statement>();
		}
		public Structure(Struct_def d, Vector<Statement> s)
		{
			def = d;
			statements =  s;
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Struct_def implements Node
	{
		Identifier id;
		Vector<String> variations;
		public Struct_def(String s)
		{
			id = new Identifier(s);
			variations = new Vector<String>();
		}
		public <T> T accept(Visitor<T> v) 
		{
		return v.visit(this);	
		}
	}
	public interface Statement extends Node
	{
		
	}
	public static class Loop implements Statement
	{
		int count;
		Location origin;
		Location distance;
		Structure s;
		String c;
		
		public Loop (int count, Location start, Location dist, Structure structure)
		{
			this.count = count;
			this.origin = new Location(start);
			this.distance = new Location(dist);
			this.s = structure;
			c = "";
		}
		public Loop (int count, Location start, Location dist, Structure structure, String variant)
		{
			this.count = count;
			this.origin = new Location(start);
			this.distance = new Location(dist);
			this.s = structure;
			c = variant;
		}
		public <T> T accept(Visitor<T> v) 
		{
		return v.visit(this);	
		}
	}
	public static class Conditional implements Statement
	{
	 Vector<Statement> statements;
	 Vector<Condition> conditions;
	 public Conditional(Vector<Statement> statement_vec, Vector<Condition> variants)
	 {
		 statements = statement_vec;
		 conditions = variants;
	 }
	 public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Condition implements Node
	{
		Identifier variant;
		public boolean equals(String s)
		{
			return (variant.id.equals(s));
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
		public Condition()
		{
			variant = new Identifier("");
		}
		public Condition(String s)
		{
			variant = new Identifier(s);
		}
		public String toString()
		{
			return variant.toString();
		}
	}
	public interface  Action extends Statement
	{
	  	
	}
	public static class Place implements Action
	{
		Identifier block;
		Location loc;
		public Place(String blockName, Location location)
		{
			block = new Identifier(blockName);
			loc = new Location(location);
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
		public String toString()
		{
			return ("Place " + block + " " + loc);
		}
	}
	public static class Remove implements Action
	{
		Location loc;
		public Remove(Location l)
		{
			loc = new Location(l);
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Use implements Action
	{
		Identifier tool;
		Location loc;
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
		public Use(String t, Location l)
		{
			tool = new Identifier(t);
			loc = new Location(l);
		}
	}
	public static class Location implements Node
	{
		int x;
		int y;
		int z;
		public Location()
		{
			x = 0;
			y = 0;
			z = 0;
		}
		public String toString()
		{
			return ("("+x+" "+y+" "+z+")");
		}
		public Location(int x_coord, int y_coord, int z_coord)
		{
			x = x_coord;
			y = y_coord;
			z = z_coord;
		}
		public Location(Location l)
		{
			x = l.x;
			y = l.y;
			z = l.z;
		}
		public void set(int x_coord, int y_coord, int z_coord)
		{
			x = x_coord;
			y = y_coord;
			z = z_coord;
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
		public void add(int x_dist, int y_dist, int z_dist)
		{
			x+= x_dist;
			y+= y_dist;
			z+= z_dist;
		}
		public void add(Location l)
		{
			x+= l.x;
			y+= l.y;
			z+= l.z;
		}
	}
	
	public static class Identifier implements Node
	{
		String id;
		public Identifier(String name)
		{
			id = name;
		}
		public String toString()
		{
			return id;
		}
		public boolean equals(Identifier i)
		{
			return (this.id.equals(i.id));
		}
		public boolean equals(String s)
		{
			return (this.id.equals(s));
		}
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}

	}
	public static class RawVisitor implements Visitor<String>
	{
		Location current_loc = new Location();
		Stack<Location> loc_stack = new Stack<Location>();
		Stack<String> cond_stack = new Stack<String>();
		String outputFileName;
		FileWriter fstream;
		BufferedWriter out;
		public RawVisitor(String outputFilename)
		{
			
			try
			{
				fstream = new FileWriter(outputFilename);
				out = new BufferedWriter(fstream);
			}
			catch(Exception e)
			{
				System.out.println("You screwed something up stupid.\n");
			}
			
		}
		
		@Override
		public String visit(Loop loop) 
		{
			current_loc.add(loop.origin);
			
			if(loop.count > 1)
			{
				
				for(int i = 0; i <loop.count; i++)
				{
					if(i>0)
					{
					current_loc.add((loop.distance.x), (loop.distance.y), (loop.distance.z));
					}
					loop.s.accept(this);
				}
			}
			else
			{
				loop.s.accept(this);
			}
			return null;
		}

		@Override
		public String visit(Condition condition) {
			return null;
		}

		@Override
		public String visit(Conditional conditional) {
			boolean condition_match = false;
			for(Condition c : conditional.conditions)
			{
				if(c.equals(cond_stack.peek()))
				{
					condition_match = true;
				}
				
			}
			if(condition_match)
			{
				for(int i = 0; i < conditional.statements.size(); i++)
				{
					conditional.statements.elementAt(i).accept(this);
				}
			}
			return null;
		}

		public String visit(Identifier identifier) {
			return null;
		}

		public String visit(Location location) {
			return null;
		}

		public String visit(Structure s) {
			for(int i = 0; i < s.statements.size(); i++)
			{
				s.statements.elementAt(i).accept(this);
			}
			return null;
		}

		public String visit(Program p) {
			for(Structure s: p.structs)
			{
				
				if(s.def.variations.size()>0)
				{
					for(String v:s.def.variations)
					{
						loc_stack.clear();
						current_loc = new Location(0,0,0); 
						loc_stack.push(current_loc);
					//System.out.println("Structure : " + s.def.id + " Variant: " + v);
						try
						{
							String w = "Structure : " + s.def.id + " Variant: " + v;
						out.write(w);
						out.newLine();
						out.flush();
						}
						catch(Exception e)
						{
							System.out.println("Hi");
						}

					cond_stack.push(v);
					s.accept(this);
					cond_stack.pop();
					}
				}
				else
				{
					loc_stack.clear();
					current_loc = new Location(0,0,0); 
					loc_stack.push(current_loc);
					System.out.println("Structure : " + s.def.id );
					s.accept(this);
				}
				
			}
			
			
			return null;
		}

		@Override
		public String visit(Struct_def s) {
			return null;
		}

		@Override
		public String visit(Place p) 
		{
			current_loc.add(p.loc);
			Identifier a = p.block;
			//System.out.println("Place " + a + current_loc);
			try
			{
			out.write("Place " + a + current_loc + "\n");
			out.newLine();
			out.flush();
			}
			catch(Exception e)
			{
			System.out.println("Bye");
			}
			
			return null;
		}

		@Override
		public String visit(Remove r) {
			current_loc.add(r.loc);
			//System.out.println("Remove "  + "(" + current_loc.x + " " + current_loc.y + " " + current_loc.z + ")");
			try
			{
			out.write("Remove "  + "(" + current_loc.x + " " + current_loc.y + " " + current_loc.z + ")");
			out.newLine();
			out.flush();
			}
			catch(Exception e)
			{
			
			}
			return null;
		}

		@Override
		public String visit(Use u) {
			current_loc.add(u.loc);
			//System.out.println("Use " + u.tool + current_loc);
			try
			{
			out.write("Use " + u.tool + current_loc);
			out.newLine();
			out.flush();
			}
			catch(Exception e)
			{
			
			}
			return null;
		}
		
	}
	public static void main(String[] args)
	{
		Vector<Condition> variants = new Vector<Condition>();
		Vector<Statement> statements = new Vector<Statement>();
		variants.add(new Condition("a"));
		variants.add(new Condition("b"));
		variants.add(new Condition("c"));
		Vector<Condition> vars2 = new Vector<Condition>();
		Vector<Statement> state2 = new Vector<Statement>();
		RawVisitor v =  new RawVisitor("out.txt");
		Structure s = new Structure(new Struct_def("dave"));
		s.def.variations = new Vector<String>();
		s.def.variations.add("a");
		s.def.variations.add("j");
		Place p = new Place("Stone", new Location(1,1,1));
		Remove r = new Remove(new Location (10, -3, 4));
		Use u = new Use("Flint and Steel", new Location(1,0,-1));
		
		
		//s.statements.add(p);
		//s.statements.add(r);
		//s.statements.add(r);
		Structure t = new Structure(new Struct_def("Bill"));
		//t.statements.add(p);
		t.statements.add(u);
		Program pro = new Program();
	
		
		//pro.structs.add(t);
	
		Loop l = new Loop(9,new Location(0,2,1), new Location(4,3,-5), t);
		//statements.add(l);
		statements.add(r);
		v.cond_stack.push("f");
		Conditional c = new Conditional(statements,variants);
		state2.add(l);
		vars2.add(new Condition("j"));
		Conditional d = new Conditional(state2, vars2);
		Conditional e = new Conditional(statements,vars2);
		s.statements.add(p);
		//s.statements.add(l);
		s.statements.add(c);
		s.statements.add(d);
		s.statements.add(e);
		pro.structs.add(s);
		//v.visit(c);
		//v.visit(p);
		//v.visit(s);
		//v.visit(r);
		//v.visit(u);
		v.visit(pro);
	}
}
