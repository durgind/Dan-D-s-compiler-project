package mine;
//import java.util.HashMap;
//import java.util.Map;
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
		Structure [] structs;

		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Structure implements Node
	{
		Struct_def def;
		Statement [] statements;
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Struct_def implements Node
	{
		Identifier id;
		String [] variations;
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
		int x_dist;
		int y_dist;
		int z_dist;
		public <T> T accept(Visitor<T> v) 
		{
		return v.visit(this);	
		}
	}
	public static class Conditional implements Statement
	{
	 Statement[] Statements;
	 Condition [] Conditions;
	 public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Condition implements Node
	{
		Identifier[] variants;
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public interface  Action extends Statement
	{
	  	
	}
	public static class Place implements Action
	{
		Identifier block;
		Location loc;
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	public static class Remove implements Action
	{
		Location loc;
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
	}
	public static class Location implements Node
	{
		int x;
		int y;
		int z;
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
	
	public static class Identifier implements Node
	{
		String id;
		public <T> T accept(Visitor<T> v) 
		{
		 return v.visit(this);	
		}
	}
}
