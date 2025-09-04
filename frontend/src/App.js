import React from 'react';
import BookSearch from './components/BookSearch';

function App() {
  return (
    <div>
      <nav className="navbar navbar-dark bg-dark">
        <div className="container-fluid">
          <span className="navbar-brand mb-0 h1">Librería Online</span>
        </div>
      </nav>
      <main>
        <BookSearch />
      </main>
    </div>
  );
}

export default App;
