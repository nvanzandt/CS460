#
# CS 460: Problem Set 5: MongoDB Query Problems
#

#
# For each query, use a text editor to add the appropriate XQuery
# command between the triple quotes provided for that query's variable.
#
# For example, here is how you would include a query that finds
# the names of all movies in the database from 1990.
#
sample = """
    db.movies.find( { year: 1990 }, 
                    { name: 1, _id: 0 } )
"""

#
# 1. Put your query for this problem between the triple quotes found below.
#    Follow the same format as the model query shown above.
#
query1 = """
    db.people.find({$or: [{name: "Ryan Gosling"}, {name: "America Ferrera"}]}, {_id: 0, name: 1, dob: 1, pob: 1 })

"""

#
# 2. Put your query for this problem between the triple quotes found below.
#
query2 = """
    db.movies.find({rating: {$exists: false}, year: {$gte: 1960}}, {_id: 0, name: 1, year: 1})

"""

#
# 3. Put your query for this problem between the triple quotes found below.
#
query3 = """
    db.oscars.distinct("movie.name", {"movie.name": /^The /})
"""

#
# 4. Put your query for this problem between the triple quotes found below.
#
query4 = """
    db.movies.aggregate( {$match: {rating: "G"}},
                         {$sort: {year: -1}},
                         {$limit: 1},
                         {$project: {_id: 0,
                                     name: 1,
                                     year: 1}})
"""

#
# 5. Put your query for this problem between the triple quotes found below.
#
query5 = """
    db.people.find({hasActed: {$exists: true}, pob: /Mexico$/ }, {_id: 0, name: 1, pob: 1} )

"""

#
# 6. Put your query for this problem between the triple quotes found below.
#
query6 = """
    db.movies.count({rating: "R", $and: [{runtime: {$gte: 120}},
                						 {runtime: {$lte: 180}}]})
"""

#
# 7. Put your query for this problem between the triple quotes found below.
#
query7 = """
    db.movies.aggregate({$match: {$and: [{runtime: {$gte: 120}},
                				 {runtime: {$lte: 180}}]}},
                        {$group: {_id: "$rating", 
                                  count: {$sum: 1}, 
                                  shortest: {$min: "$runtime"}, 
                                  longest: {$max: "$runtime"}}},
                        {$match: {_id: {$ne: null}}},
                        {$sort: {_id: 1}},
                        {$project: {_id: 0, 
                                    num_2_3_hours: "$count",
                                    shortest: 1,
                                    longest: 1,
                                    rating: "$_id"}})
"""

#
# 8. Put your query for this problem between the triple quotes found below.
#
query8 = """
    db.people.aggregate({$match: {hasDirected: {$exists: true}, pob: /USA$/}},
                    {$group: {_id: "$pob",
                              count: {$sum: 1}}},
                    {$match: {count: {$gte: 5}}},
                    {$sort: {count: -1, pob: 1}},
                    {$project: {_id: 0,
                                num_directors: "$count",
                                pob: "$_id"}})
"""

#
# 9. Put your query for this problem between the triple quotes found below.
#
query9 = """
    db.movies.aggregate({$unwind: "$directors"},
                    {$match: {"directors.name": "Christopher Nolan"}},
                    {$group: {_id: 0,
                              num_movies: {$sum: 1},
                              movies: {$addToSet: "$name"},
                              avg_runtime: {$avg: "$runtime"},
                              top_rank: {$min: "$earnings_rank"}}},
                    {$project: {_id: 0,
                                num_movies: 1,
                                movies: 1,
                                avg_runtime: 1,
                                top_rank: 1}})

"""

#
# 10. Put your query for this problem between the triple quotes found below.
#
query10 = """
    db.movies.aggregate({$match: {earnings_rank: {$lte: 200}}},
                    {$unwind: "$directors"},
                    {$group: {_id: "$directors.name",
                     		  count: {$sum: 1}}},
                    {$sort: {count: -1, _id: 1}},
                    {$limit: 4},
                    {$project: {_id: 0,
                                num_top_grossers: "$count",
                                director: "$_id"}})
"""
