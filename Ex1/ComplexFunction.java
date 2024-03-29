package Ex1;

import java.util.Stack;

public class ComplexFunction implements complex_function 
{
	Operation op;
    private function left;
    private function right;
    
 //******************  Constructors  ****************  
    /**
     * Regular constructor
     * @param o operation
     * @param l left
     * @param r right
     */
	public ComplexFunction(Operation o,function l,function r )
	{
	this.op=o; 
	this.left=l.copy();
	this.right=r.copy();
	if(this.left != null && (!(this.right.toString().equals("0"))) && this.op == Operation.None)
        throw new RuntimeException("Error:invaild operation!!\n this shape is not alowwed: None(0,b) , None(a,b)\nwhile b,a not equal 0");
	
		
	}
    
	
	
    /**
     * Constructor from on side function.
     * @param f
     */
	public ComplexFunction(function f) 
	{
		this.left=f.copy();
		this.right=new Polynom("0.0");
		this.op= Operation.None;
	}

	
	

	/**
	 * Constructor with Op as a String.
	 * @param op - string
	 * @param l
	 * @param r
	 */
	public ComplexFunction(String op, function l, function r) 
	{
		this.op=StringtoOp(op);    
		this.left=l.copy();
		this.right=r.copy();
		if(this.left != null && (!(this.right.toString().equals("0"))) && this.op == Operation.None)
	        throw new RuntimeException("Eror:invaild operation!!\\n this shape is not alowwed: None(0,b) , None(a,b)\\nwhile b,a not equal 0");
		
	}

	
	

	/**
	 * Constructor that got only string
	 * @param string
	 */
	public ComplexFunction(String string) 
	{
		ComplexFunction cf =(ComplexFunction) initFromString(string);
		this.left=cf.left();
		this.op=cf.getOp();
		this.right=cf.right();
	}



	/**
	 * Convert String of Op to a Op Object
	 * @param op1 -string
	 * @return op object
	 */
	private Operation StringtoOp(String op1) 
	{
		op1=op1.toLowerCase(); //normalize the string for good input.
		
		if(op1.equals("plus"))
			return Operation.Plus;
		
		else if(op1.equals("times")||op1.equals("mul"))
			return Operation.Times;
		
		else if(op1.equals("div")||op1.equals("divid"))
			return Operation.Divid;
		
		else if(op1.equals("max"))
			return Operation.Max;
		
		else if(op1.equals("min"))
			return Operation.Min;
		
		else if(op1.equals("comp"))
			return Operation.Comp;
		
		else if(op1.equals("none"))
			return Operation.None;
		else 
			throw new RuntimeException("Eror: Invalid Operator!!");
	}




//*********** More Opreation*****************************
	
	/**
	 * return the value of function in the current x.
	 * 
	 */
	@Override
	public double f(double x)
	{
		double res=0;
		
		switch(this.op) //switch case acorrding to the Opreation Enum.
		
		{
		
		case Plus:
			return res+this.left.f(x)+this.right.f(x);
			
			
		case Times:
			return res+ this.left.f(x)*this.right.f(x);
			
			
		case Divid:
			if(this.right.f(x)==0)
				throw new ArithmeticException("Eror:Dont divide by 0 !!!");
			return res+ this.left.f(x)/this.right.f(x);
			
			
		case Max:
			return res+ Math.max(this.left.f(x), this.right.f(x));
			
			
		case Min:
			return res+ Math.min(this.left.f(x), this.right.f(x));
			
			
		case Comp:
			return res+ this.left.f(this.right.f(x));
			
			
		case None:
			if(this.left != null && this.right.toString().equals("0")) //only from the shape None(a,0). and not from the shape None(a,b)
				return res+ this.left.f(x);
			else
			throw new RuntimeException("Error:invalid Opreation!!");
			
			
		case Error:
			throw new RuntimeException("Error:invalid Opreation!!");
			
			
		}
		return res;
	}

	
	/**
	 * Build a ComplexFunction from a String.
	 * use an RecursionFunction to do it;
	 */
	@Override
	public function initFromString(String s)
	{
		s=s.toLowerCase();
		function f;
		if(s.contains(",")==false)// the string is just polynom and not complex
		{
			Polynom p1 = new Polynom(s);
			return p1;
		}
		
		else if(PatternIsBalanced(s)==false)
			throw new RuntimeException("Eror: the pattern of the string is uncorrect!!");
		
		else
			f=FromStringRecursion(s);
		
			
			
		return f;
	}

	
	
	/**
	 * RecursionFunction help the funtion InitFromString to do her role.
	 * @param s string
	 * @return recursion call
	 */
	private function FromStringRecursion(String s)
	{
		String left;
		String right;
		int start;
		int end;
		
		start=s.indexOf('(');
		String operator=s.substring(0,start);
		Operation o = StringtoOp(operator);
		
		
		end=MiddlePsik(s); 
		
		
		left=s.substring(start+1, end); 
		
		
		right=s.substring(end+1, s.length()-1); 
		
		
		return new ComplexFunction(o,initFromString(left),initFromString(right) );
		
	}

	/**
	 * HelperFunction that used to find the correct comma which in the middle between left&right
	 * @param s
	 * @return
	 */
	private int MiddlePsik(String s) 
	{
		Stack <Character> MiddleStack = new Stack <Character>();
		
		if (!s.contains("(")) 
			return s.indexOf(",")+1;

		int start= s.indexOf('(');
		int psik= s.indexOf(',');//maybe to delete
		if (psik<start)
			return psik+1;
		
		//MiddleStack.push('(');
		for (int i = start+1; i < s.length()-1; i++)
		{
			
			char letter= s.charAt(i);
			
			if (letter=='(')
				MiddleStack.push(letter);
			if (letter == ')') 
				MiddleStack.pop();
			if (MiddleStack.empty() &&letter == ',')// need to check.
				return i;//+1
			
		}
		return -1;
	}



	/**
	 * Check if the pattern of the string is Blanced and correct
	 * @param s string
	 * @return True/False
	 */
	private boolean PatternIsBalanced(String s) 
	{

		Stack<Character> stack = new Stack<Character>(); //Parenthesis Stack
		Stack<Character> stack2 = new Stack<Character>(); //Comma Stack
		for (int i = 0; i < s.length(); i++)
		{	
			//Parenthesis Balanced
			char current = s.charAt(i);
			if (current == '(')	
			{
				stack.push(current);
				stack2.push(current);
			}
			
			if (current == ')')
			 {
			 if (stack.isEmpty())
				 return false;
			else
				stack.pop();
			 }
			
			//Comma Balcnced
			if (current == ',')
			{
				if(stack2.isEmpty()==true)
					return false;
				else
					stack2.pop();
			}
				
					
				
		}
		
		
	if(stack.isEmpty()&&stack2.isEmpty())
		return true;
	else
		return false;
}
		
		
		
		

	@Override
	/**
	 * doing a DeepCopy to the function.
	 * 
	 */
	public function copy() 
	{
		function l = this.left.copy();
		function r = this.right.copy();
		return new ComplexFunction(this.op,l,r);
	}
	
	
	
	/**
	 * Create from the function to String.
	 * 
	 */
	public String toString() 
	{
		String str;
		if(!(this.op==Operation.None && this.right.toString().equals("0")))
			str= this.op + "("+ this.left.toString()+","+this.right.toString()+")";
		else
			str=this.left.toString(); //None(a,0)= a
		return str;
		
	}
	
	
	
	/**Importnat Attention: according to the message of the teachers, we cant check 100% equalitvy of complex
	 * functions. for that reason i did check of 10,000 diffrent value and if there 1 value that not stay the functions equal
	 * i returned false
	 * 
	 */
	public boolean equals(Object obj)
	{
		function fnc;
		if(!(obj instanceof function))
			return false;
		else
			fnc = (function)obj;
		
		for(int i=1;i<10000;i++)
		{
			if(this.f(i)!=fnc.f(i)) 
				return false;
		}
	
		return true;
	}
	
	
	
//********************* Arithmetic Operation************
	@Override
	public void plus(function f1)
	{
		
		this.left=this.copy();
		this.right=f1;
		this.op=Operation.Plus;
		
	}

	@Override
	public void mul(function f1) 
	{
		this.left=this.copy();
		this.right=f1;
		this.op=Operation.Times;
		
	}

	@Override
	public void div(function f1)
	{
		this.left=this.copy();
		this.right=f1;
		this.op=Operation.Divid;
		
	}

	@Override
	public void max(function f1)
	{
		this.left=this.copy();
		this.right=f1;
		this.op=Operation.Max;
		
	}

	@Override
	public void min(function f1) 
	{
		this.left=this.copy();
		this.right=f1;
		this.op=Operation.Min;
		
	}

	@Override
	public void comp(function f1) 
	{
		this.left=this.copy();
		this.right=f1;
		this.op=Operation.Comp;
		
	}
	
	
	
//******************* Getters ****************************
	@Override
	public function left()
	{
		
		return this.left;
	}

	
	@Override
	public function right() 
	{
		
		return this.right;
	}

	
	@Override
	public Operation getOp() 
	{
		
		return this.op;
	}

}
