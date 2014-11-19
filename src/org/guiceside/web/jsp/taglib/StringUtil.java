package org.guiceside.web.jsp.taglib;


/**
 * General string utils.
 */
public final class StringUtil
{
     // ---------------------------------------------------------------- ignore cases

    /**
     * Finds first index of a substring in the given source string with ignored case.
     *
     * @param src  source string for examination
     * @param subS substring to find
     * @return index of founded substring or -1 if substring is not found
     * @see #indexOfIgnoreCase(String, String, int)
     */
    public static int indexOfIgnoreCase(String src, String subS)
    {
        return indexOfIgnoreCase(src, subS, 0);
    }

    /**
     * Finds first index of a substring in the given source string with ignored
     * case. This seems to be the fastest way doing this, with common string
     * length and content (of course, with no use of Boyer-Mayer type of
     * algorithms). Other implementations are slower: getting char array frist,
     * lowercasing the source string, using String.regionMatch etc.
     *
     * @param src        source string for examination
     * @param subS       substring to find
     * @param startIndex starting index from where search begins
     * @return index of founded substring or -1 if substring is not found
     */
    public static int indexOfIgnoreCase(String src, String subS, int startIndex)
    {
        String sub = subS.toLowerCase();
        int sublen = sub.length();
        int total = src.length() - sublen + 1;

        for (int i = startIndex; i < total; i++)
        {
            int j = 0;

            while (j < sublen)
            {
                char source = Character.toLowerCase(src.charAt(i + j));

                if (sub.charAt(j) != source)
                {
                    break;
                }

                j++;
            }

            if (j == sublen)
            {
                return i;
            }
        }

        return -1;
    }


  
}
