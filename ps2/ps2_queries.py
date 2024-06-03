#
# CS 460: Problem Set 2: XQuery Programming Problems
#

#
# For each query, use a text editor to add the appropriate XQuery
# command between the triple quotes provided for that query's variable.
#
# For example, here is how you would include a query that finds
# the names of all movies in the database from 1990.
#
sample = """
    for $m in //movie
    where $m/year = 1990
    return $m/name
"""

#
# 1. Put your query for this problem between the triple quotes found below.
#    Follow the same format as the model query shown above.
#
query1 = """
    //person[contains(name, "Sam ")]/name
"""

#
# 2. Put your query for this problem between the triple quotes found below.
#
query2 = """
    for $p in //person 
    for $o in //oscar
    for $m in //movie 
    where $p/@id = $o/@person_id and $o/@movie_id = $m/@id and $p/name = "Jodie Foster"
    return <foster_oscar>
        {
            $m/name,
            $o/type,  
            $o/year   
            }
        </foster_oscar>
"""

#
# 3. Put your query for this problem between the triple quotes found below.
#
query3 = """
    for $year in distinct-values(//movie/year)
    let $movies := //movie[year = $year]
    where $year > 2009 
    order by $year
    return 
    <runtime_info>
    { 
        <year>
        {$year}
        </year>,
        <average>
        {avg($movies/runtime)}
        </average>,
        <longest>
        {max($movies/runtime)}
        </longest>,
        <shortest>
        {min($movies/runtime)}
        </shortest>
    }
    </runtime_info>
"""

#
# 4. Put your query for this problem between the triple quotes found below.
#
query4 = """
    for $p in //person
    let $p_movies := //movie[contains(@directors, $p/@id) and earnings_rank <= 200]
    where count($p_movies) >= 4
    order by $p/name
    return <top_director>
        {
            $p/name,
            <num_top_grossers>
            {count ($p_movies)}
            </num_top_grossers>,
            for $m in $p_movies
            return <movie>
                    {string($m/name),"-", string($m/earnings_rank)}
                    </movie>        
        }
        </top_director>
"""

#
# 5. Put your query for this problem between the triple quotes found below.
#
query5 = """
    for $m in //movie 
    where $m[contains($m/genre, "B")]
    order by $m/name
    return <biopic>
        {
            $m/name,
            $m/year,
            for $o in //oscar
            where $o/@movie_id = $m/@id
            return <award>
                    {string($o/type)}
                    </award>
        }
    </biopic>
"""
