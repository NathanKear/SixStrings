const { ApolloServer, gql } = require('apollo-server');

// A schema is a collection of type definitions (hence "typeDefs")
// that together define the "shape" of queries that are executed against
// your data.
const typeDefs = gql`
  # Comments in GraphQL strings (such as this one) start with the hash (#) symbol.

  # This "Book" type defines the queryable fields for every book in our data source.
  type Book {
    title: String
    author: String
  }

  type Song {
      name: String!
      artist: String!
      id: ID!
  }

  # The "Query" type is special: it lists all of the available queries that
  # clients can execute, along with the return type for each. In this
  # case, the "books" query returns an array of zero or more Books (defined above).
  type Query {
    books: [Book]
    songs: [Song!]!
  }
`;

const books = [
    {
      title: 'Harry Potter and the Chamber of Secrets',
      author: 'J.K. Rowling',
    },
    {
      title: 'Jurassic Park',
      author: 'Michael Crichton',
    },
  ];

// Resolvers define the technique for fetching the types defined in the
// schema. This resolver retrieves books from the "books" array above.
const resolvers = {
    Query: {
      books: () => books,
      songs: () => songs,
    },
  };

const songs = [
    { name: "Wish You Were Here", artist: "Pink Floyd", id: "21484de1-6bcd-42d1-812e-600a29e1c473" },
    { name: "Airbag", artist: "Radiohead", id: "027070fd-6b26-411e-b7b8-245b80392cc1" },
    { name: "Cycling Trivialities", artist: "José González", id: "ae51910a-0fbf-4eae-9851-a68e9b99cabb" },
    { name: "With The Ink Of A Ghost", artist: "José González", id: "7f98b43f-43fb-49f7-b1c9-046a027f496f" },
    { name: "Viðrar vel til loftárása", artist: "Sigur Rós", id: "3fac9b17-802a-4e35-928b-cebaaa133566" },
    { name: "Ágætis byrjun", artist: "Sigur Rós", id: "698f58bf-41f5-4330-9801-3b94d8733a76" },
    { name: "Perth", artist: "Bon Iver", id: "28978164-6547-4cf4-a98a-07d2353162ae" },
    { name: "Brokeback Mountain Theme", artist: "Gustavo Santaolalla", id: "c488c941-38a4-465b-9a23-2ca8c1e57a55" },
    { name: "Champagne Supernova", artist: "Oasis", id: "ce3600cf-b430-494b-b6f8-bc82b03814ce" },
    { name: "The Last Of Us Theme", artist: "Gustavo Santaolalla", id: "6a43ba38-1a31-4a39-9722-d374756f5ca7" },
    { name: "Caring Is Creepy", artist: "The Shins", id: "344df1ca-59ec-4167-92f3-88fe8ec76175" },
    { name: "New Slang", artist: "The Shins", id: "5b8854db-a997-4a30-b656-fd9701cb379a" },
    { name: "Feeling You", artist: "Harrison Storm", id: "6edc165c-16f3-4335-ade4-ba3a7083a2c0" },
    { name: "Mean To Me", artist: "Stella Donnelly", id: "699eae38-a210-4274-85a7-3ebb6970beec" },
    { name: "The Barrel", artist: "Aldous Harding", id: "5699a096-7bfe-45dc-93e0-d17b03976ede" },
    { name: "Chasing Gold", artist: "Dustin Tebbutt", id: "0127e0d6-baa9-423b-9692-92027fa6827c" },
    { name: "Broke Machine", artist: "Tori Forsyth", id: "3d17f651-1da1-44ce-b3e3-00f3a82c04b0" },
]

// The ApolloServer constructor requires two parameters: your schema
// definition and your set of resolvers.
const server = new ApolloServer({ typeDefs, resolvers });

// The `listen` method launches a web server.
server.listen().then(({ url }) => {
  console.log(`🚀  Server ready at ${url}`);
});