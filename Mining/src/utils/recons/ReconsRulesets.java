package utils.recons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import compiler.Compiler;
import completer.Completion;
import game.Game;
import grammar.Grammar;
import main.FileHandling;
import main.StringRoutines;
import main.grammar.Description;
import main.grammar.Report;
import main.grammar.Symbol;
import other.GameLoader;
import other.concept.Concept;

/**
 * To reconstruct rulesets.
 *
 * @author Eric.Piette
 */
public class ReconsRulesets
{
	public static void main(final String[] args)
	{
		String outputPath = args.length == 0 ?  "./res/recons/output/" : args[0];
		int numRecons = args.length < 1 ?  10 : Integer.parseInt(args[1]);
		int numReconsNoWarning = args.length < 2 ?  1 : Integer.parseInt(args[2]);
		int numReconsNoWarningExpectedConcepts = args.length < 3 ?  1 : Integer.parseInt(args[3]);
		int maxNumberAttempts = args.length < 4 ?  10000 : Integer.parseInt(args[4]);
		
		System.out.println("\n=========================================\nTest: Start reconstruction all of rulesets:\n");

		final long startAt = System.nanoTime();

		// Load from memory
		final String[] choices = FileHandling.listGames();
		CompleterWithPrepro completer = new CompleterWithPrepro();

		for (final String fileName : choices)
		{
			//if (!fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/reconstruction/"))
			//if (!fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/reconstruction/board/hunt/Fortresse"))
			if (!fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/reconstruction/board/space/line/Ashanti Alignment Game"))
			//if (!fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/reconstruction/board/hunt/Bagh Bukree"))
			//if (!fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/test/eric/recons/test"))
				continue;
			
			final String gameName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length()-4);
			
			// Get game description from resource
			System.out.println("Game: " + gameName);

			String path = fileName.replaceAll(Pattern.quote("\\"), "/");
			path = path.substring(path.indexOf("/lud/"));

			String desc = "";
			String line;
			try
			(
				final InputStream in = GameLoader.class.getResourceAsStream(path);
				final BufferedReader rdr = new BufferedReader(new InputStreamReader(in));
			)
			{
				while ((line = rdr.readLine()) != null)
					desc += line + "\n";
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			
			final String metadata = desc.contains("(metadata") ? desc.substring(desc.indexOf("(metadata")) : "";
			String idStr = metadata.contains("(id") ? metadata.substring(metadata.indexOf("(id")+ 5) : "";
			idStr = idStr.substring(0, idStr.indexOf(')')-1);
			final int idRulesetToRecons = Integer.valueOf(idStr).intValue();
			
			int numAttempts = 0;
			List<Completion> compilingCompletions = new ArrayList<Completion>();
			List<Completion> compilingNoWarningCompletions = new ArrayList<Completion>();
			List<Completion> compilingNoWarningExpectedConceptsCompletions = new ArrayList<Completion>();
			
			final List<Concept> trueConcepts = computeTrueConcepts(desc);
			System.out.println("The true concepts of this reconstruction are:");
			for(Concept concept: trueConcepts)
				System.out.println(concept);
			
			// Run the recons process until enough attempts is executed or reconstruction are generated.
			while(numAttempts < maxNumberAttempts && 
					(compilingCompletions.size() < numRecons ||
							compilingNoWarningCompletions.size() < numReconsNoWarning ||
							compilingNoWarningExpectedConceptsCompletions.size() < numReconsNoWarningExpectedConcepts)
			)
			{
				List<Completion> completions = null;
				try
				{
					completions = completer.completeSampled(desc, 1, idRulesetToRecons, null);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}

				if (completions != null)
				{
					for (int n = 0; n < completions.size(); n++) 
					{
						final Completion completion = completions.get(n);
						final String completionRaw = indentNicely(StringRoutines.unformatOneLineDesc(completion.raw()));
						// Test if the completion compiles.
						Game game = null;
						//System.out.println(completionRaw);
						try{game = (Game) Compiler.compileTest(new Description(completionRaw), false);}
						catch(final Exception e)
						{
//							System.out.println("Impossible to compile number "+ n);
//							System.out.println("DESC IS");
//							System.out.println(completionRaw);
//							e.printStackTrace();
						}
						if(game != null)
						{
							final String rawDescMetadata = completionRaw + "\n" + metadata;
							completions.get(n).setRaw(rawDescMetadata);
							compilingCompletions.add(completions.get(n));
							
							System.out.print("One Completion found");
							
							if(!game.hasMissingRequirement() && !game.willCrash())
							{
								System.out.print( " with no warning");
								compilingNoWarningCompletions.add(completions.get(n));
							}
							game = (Game) Compiler.compileTest(new Description(rawDescMetadata), false);
							
							// Check if the concepts expected are present.
							boolean expectedConcepts = Concept.isExpectedConcepts(rawDescMetadata);
							if(expectedConcepts)
							{
								compilingNoWarningExpectedConceptsCompletions.add(completions.get(n));
								System.out.print( " and with the expected concepts");
							}
							System.out.println();
						}
					}
				}
				numAttempts++;
			}

			// We rank the completions.
			Collections.sort(compilingCompletions, (c1, c2) -> c1.score() < c2.score() ? 1 : c1.score() == c2.score() ? 0 : -1);
			
			for (int n = 0; n < compilingCompletions.size(); n++) 
			{
				System.out.println("Completion " + n + " has a score of " + compilingCompletions.get(n).score());
				if(compilingNoWarningExpectedConceptsCompletions.contains(compilingCompletions.get(n)))
					CompleterWithPrepro.saveCompletion(outputPath + gameName + "/" + "noWarning/"+ "expectedConcepts/", gameName + n, compilingCompletions.get(n).raw());
				else if(compilingNoWarningCompletions.contains(compilingCompletions.get(n)))
					CompleterWithPrepro.saveCompletion(outputPath + gameName + "/" + "noWarning/", gameName + n, compilingCompletions.get(n).raw());
				else
					CompleterWithPrepro.saveCompletion(outputPath + gameName + "/", gameName + n, compilingCompletions.get(n).raw());
			}

			System.out.println("Num Attempts = " + numAttempts);
			
			System.out.println(compilingCompletions.size() + " recons generated, " + compilingNoWarningCompletions.size() + " recons without warning generated.");
		}
		
		final long stopAt = System.nanoTime();
		final double secs = (stopAt - startAt) / 1000000000.0;
		System.out.println("\nDone in " + secs + "s.");

//		if (!failedGames.isEmpty())
//		{
//			System.out.println("\nUncompiled games:");
//			for (final String name : failedGames)
//				System.out.println(name);
//		}
	}

	//-----------------------------------------------------------------------------
	
	/**
	 * Nicely indents the description in entry.
	 */
	public static String indentNicely(final String desc)
	{
		final String linesArray[] = desc.split("\\r?\\n");
		final List<String> lines = new ArrayList<String>();
        for (int n = 0; n < linesArray.length; n++)
        	lines.add(linesArray[n]);
		          
        // Left justify all lines
        for (int n = 0; n < lines.size(); n++)
        {
        	String str = lines.get(n);
        	final int c = 0;
        	while (c < str.length() && (str.charAt(c) == ' ' || str.charAt(c) == '\t'))
        		str = str.substring(1);
        	
        	lines.remove(n);
        	lines.add(n, str);
        }

        removeDoubleEmptyLines(lines);
        indentLines(lines);
        
        final StringBuffer outputDesc = new StringBuffer();
        for (final String result : lines)
        	outputDesc.append(result + "\n");
        
        return outputDesc.toString();
    }
	
	/**
	 * Removes double empty lines.
	 */
	final static void removeDoubleEmptyLines(final List<String> lines)
	{
        int n = 1;
        while (n < lines.size())
        {
        	if (lines.get(n).equals("") && lines.get(n-1).equals(""))
        		lines.remove(n);
        	else
        		n++;
        }
 	}
	
	/**
	 * Nicely indents the lines of a desc.
	 */
	final static void indentLines(final List<String> lines)
	{
		final String indentString = "    ";
        int indent = 0;
        for (int n = 0; n < lines.size(); n++)
        {
        	String str = lines.get(n);
        	
        	final int numOpen  = StringRoutines.numChar(str, '(');  // don't count curly braces!
        	final int numClose = StringRoutines.numChar(str, ')');
        	
        	final int difference = numOpen - numClose;
        	
        	if (difference < 0)
        	{
        		// Unindent from this line
        		indent += difference;
        		if (indent < 0)
        			indent = 0;
        	}
        	        	
        	for (int step = 0; step < indent; step++)
        		str = indentString + str; 
 
   	       	lines.remove(n);
        	lines.add(n, str);
        	
        	if (difference > 0)
        		indent += difference;  // indent from next line
        }
	}
	
	/**
	 * @param desc The recons desc of the game.
	 * @return The list of concepts which are sure to be true for a recons description.
	 */
	final static List<Concept> computeTrueConcepts(final String desc)
	{
		final List<Concept> trueConcepts = new ArrayList<Concept>();
		
		// Keep only the game description.
		String descNoMetadata = desc.substring(0,desc.lastIndexOf("(metadata"));
		descNoMetadata = descNoMetadata.substring(0, descNoMetadata.lastIndexOf(')'));

		Description description = new Description(descNoMetadata);
		CompleterWithPrepro.expandRecons(description);
		descNoMetadata = description.expanded();
		
		// Get all the ludemeplexes between parenthesis.
		final List<String> ludemeplexes = new ArrayList<String>();
		for(int i = 0; i < descNoMetadata.length(); i++)
		{
			final char c = descNoMetadata.charAt(i);
			if(c == '(')
			{
				int countParenthesis = 1;
				int indexCorrespondingParenthesis = i+1;
				for(; indexCorrespondingParenthesis < descNoMetadata.length(); indexCorrespondingParenthesis++)
				{
					if(descNoMetadata.charAt(indexCorrespondingParenthesis) == '(')
						countParenthesis++;
					else
						if(descNoMetadata.charAt(indexCorrespondingParenthesis) == ')')
							countParenthesis--;
					if(countParenthesis == 0)
					{
						indexCorrespondingParenthesis++;
						break;
					}
				}
				final String ludemeplex = descNoMetadata.substring(i, indexCorrespondingParenthesis);
				if(!ludemeplex.contains("#") && !ludemeplex.contains("[") && !ludemeplex.contains("]"))
					ludemeplexes.add(ludemeplex);
			}
		}
		
		for(String ludemeplex : ludemeplexes)
		{
			for(Concept concept: getTrueConcepts(ludemeplex))
			{
				if(!trueConcepts.contains(concept))
					trueConcepts.add(concept);
			}
		}
		
		return trueConcepts;
	}

	//----------------------CODE TO GET THE CONCEPTS OF A STRING (TO MOVE TO ANOTHER CLASS LATER)------------------------------------
	
	/**
	 * @param str The description of the ludemeplex.
	 * @return The true concepts of the ludemeplex.
	 */
	static List<Concept> getTrueConcepts(final String str)
	{
		final List<Concept> trueConcepts = new ArrayList<Concept>();
		
		if (str == null || str.equals(""))
			return trueConcepts;

		try
		{
			final Object compiledObject = compileString(str);
			if (compiledObject != null)
			{
				trueConcepts.addAll(evalConceptCompiledObject(compiledObject));
			}
		}
		catch (final Exception ex)
		{
			// Nothing to do.
		}
		
		return trueConcepts;
	}
	
	/**
	 * Attempts to get the concepts from a ludemeplex.
	 */
	static List<Concept> evalConceptCompiledObject(final Object obj)
	{
		final Game tempGame = new Game("test", new Description("(game \"Tic-Tac-Toe\" (players 2) (equipment { (board (square 3)) "
				+ "	          (piece \"Disc\" P1) (piece \"Cross\" P2) }) (rules (play (move Add (to "
				+ "	          (sites Empty)))) (end (if (is Line 3) (result Mover Win))) ) )"));
		
		final List<Concept> trueConcepts = new ArrayList<Concept>();
		
		// Need to preprocess the ludemes before to call the eval method.
		Method preprocess = null;
		try
		{
			preprocess = obj.getClass().getDeclaredMethod("preprocess", tempGame.getClass());
			if (preprocess != null)
				preprocess.invoke(obj, tempGame);
		}
		catch (final Exception e)
		{
			// Nothing to do.
		}
		
		Method conceptMethod = null;
		BitSet concepts = new BitSet();
		try
		{
			conceptMethod = obj.getClass().getDeclaredMethod("concepts", tempGame.getClass());
			if (preprocess != null)
				concepts = ((BitSet) conceptMethod.invoke(obj, tempGame));
		}
		catch (final Exception e)
		{
			//e.printStackTrace();
		}
		
		for (int i = 0; i < Concept.values().length; i++)
		{
			final Concept concept = Concept.values()[i];
			if (concepts.get(concept.id()))
				trueConcepts.add(concept);
		}

		return trueConcepts;
	}
	
	/**
	 * Attempts to compile a given string for every possible symbol class.
	 * @return Compiled object if possible, else null.
	 */
	static Object compileString(final String str)
	{
		Object obj = null;
		
		final String token = StringRoutines.getFirstToken(str);
		final List<Symbol> symbols = Grammar.grammar().symbolsWithPartialKeyword(token);

		// Try each possible symbol for this token
		for (final Symbol symbol : symbols)
		{
			final String className = symbol.cls().getName();
			final Report report = new Report();
			
			try
			{
				obj = Compiler.compileObject(str, className, report);
			}
			catch (final Exception ex)
			{
				//System.out.println("Couldn't compile.");
			}
				
			if (obj == null)
				continue;
		}
		
		return obj;
	}


}
