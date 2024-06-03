#
# CS 460: Problem Set 1, SQL Programming Problems
#

#
# For each problem, use a text editor to add the appropriate SQL
# command between the triple quotes provided for that problem's variable.
#
# For example, here is how you would include a query that finds the
# names and years of all movies in the database with an R rating:
#
sample = """
    SELECT name, year
    FROM Movie
    WHERE rating = 'R';
"""

#
# Problem 4. Put your SQL command between the triple quotes found below.
#
problem4 = """
    SELECT name, dob, pob
    FROM Person
    Where name = 'Ryan Gosling' or name = 'America Ferrera';
"""

#
# Problem 5. Put your SQL command between the triple quotes found below.
#
problem5 = """
    SELECT rating, count(*)
    FROM Movie
    WHERE rating IS NOT NULL 
        AND runtime > 180
    GROUP BY rating;
"""

#
# Problem 6. Put your SQL command between the triple quotes found below.
#
problem6 = """
    SELECT M.name, O.type, O.year
    FROM Movie M, Oscar O, Person P
    Where M.id = O.movie_id
        AND P.id = O.person_id
        And p.name = 'Jodie Foster';
"""

#
# Problem 7. Put your SQL command between the triple quotes found below.
#
problem7 = """
    SELECT count(DISTINCT DirP.name)
    FROM Director D, Movie M, Actor A, Person DirP, Person ActP
    WHERE D.movie_id = M.id 
        AND A.movie_id = M.id
        AND DirP.id = D.director_id
        And ActP.id = A.actor_id 
        AND DirP.pob NOT LIKE '%USA' 
        AND ActP.pob NOT LIKE '%USA';
"""

#
# Problem 8. Put your SQL command between the triple quotes found below.
#
problem8 = """
    SELECT year, avg(runtime)
    FROM Movie 
    GROUP BY year
    HAVING year > 2009
        ORDER BY year ASC;
"""

#
# Problem 9. Put your SQL command between the triple quotes found below.
#
problem9 = """
    SELECT DISTINCT(M.name), M.year
    From Movie M, Oscar O
    Where M.id = O.movie_id
        AND M.name LIKE "THE %";
"""

#
# Problem 10. Put your SQL command between the triple quotes found below.
#
problem10 = """
    SELECT count(*)
    FROM Movie M
    WHERE M.runtime < (SELECT min(M.runtime)
                       FROM Movie M, Oscar O
                       WHERE M.id = O.movie_id
                       AND type = "BEST-PICTURE");
"""

#
# Problem 11. Put your SQL command between the triple quotes found below.
#
problem11 = """
    SELECT P.name, count(*)
    FROM Director D, Movie M, Person P
    WHERE D.movie_id = M.id
        AND D.director_id = P.id
        AND M.earnings_rank <= 200
    GROUP BY P.name 
    HAVING count(*) >= 4
        ORDER BY count(*) DESC;
"""

#
# Problem 12. Put your SQL command between the triple quotes found below.
#
problem12 = """
    SELECT count(DISTINCT A.actor_id)
    FROM Actor A
    WHERE A.actor_ID NOT IN (SELECT A.actor_id
                            FROM Movie M, Actor A
                            WHERE M.id = A.movie_id
                                AND M.earnings_rank <= 200 );     
"""

#
# Problem 13. Put your SQL command between the triple quotes found below.
#
problem13 = """
    SELECT M.name, O.type
    FROM Movie M LEFT OUTER JOIN Oscar O ON M.id = O.movie_id
    WHERE M.genre LIKE '%B%';
"""

#
# Problem 14. Put your SQL command between the triple quotes found below.
#
problem14 = """
    SELECT name, year
    FROM Movie 
    Where rating = 'G'
    AND year =	(SELECT max(year)
                 FROM Movie
                 WHERE rating = 'G');        
"""

#
# Problem 15. Put your SQL command between the triple quotes found below.
#
problem15 = """
    UPDATE MOVIE 
    SET rating = 'PG-13' 
    WHERE name = 'Indiana Jones and the Temple of Doom';
"""
