
import React, { useState } from 'react';
import axios from 'axios';

// A simple component to display a single book
const BookCard = ({ book, onAddReview, onAddRating }) => {
    const averageRating = book.ratings && book.ratings.length > 0
        ? (book.ratings.reduce((acc, rating) => acc + rating.rating, 0) / book.ratings.length).toFixed(1)
        : 'N/A';

    return (
        <div className="card mb-3">
            <div className="card-body">
                <h5 className="card-title">{book.title}</h5>
                <h6 className="card-subtitle mb-2 text-muted">por {book.author}</h6>
                <p className="card-text"><strong>ISBN:</strong> {book.isbn}</p>
                <p className="card-text"><strong>Calificación promedio:</strong> {averageRating}</p>
                <div>
                    <h6>Reseñas:</h6>
                    {book.reviews && book.reviews.length > 0 ? (
                        <ul className="list-group list-group-flush">
                            {book.reviews.map(review => (
                                <li key={review.id} className="list-group-item">{review.reviewText}</li>
                            ))}
                        </ul>
                    ) : (
                        <p>Aún no hay reseñas.</p>
                    )}
                </div>
                <button className="btn btn-primary mt-3" onClick={() => onAddReview(book)}>Añadir reseña</button>
                <button className="btn btn-info mt-3 ms-2" onClick={() => onAddRating(book)}>Añadir calificación</button>
            </div>
        </div>
    );
};


const BookSearch = () => {
    const [searchParams, setSearchParams] = useState({ term: '', title: '', author: '', isbn: '' });
    const [books, setBooks] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // State for the review modal
    const [reviewModalOpen, setReviewModalOpen] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [reviewText, setReviewText] = useState('');
    const [previewReview, setPreviewReview] = useState(false);

    // State for the rating modal
    const [ratingModalOpen, setRatingModalOpen] = useState(false);
    const [rating, setRating] = useState(0);
    


    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setBooks([]);

        try {
            const query = new URLSearchParams(searchParams).toString();
            const response = await axios.get(`http://localhost:8080/api/books/search?${query}`);
            setBooks(response.data);
        } catch (err) {
            setError('Error al cargar los libros. Asegúrate de que el backend esté funcionando.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleAddReview = async (e) => {
        e.preventDefault();
        if (!reviewText || !selectedBook) return;

        try {
            const { data: newReview } = await axios.post('http://localhost:8080/api/reviews', {
                bookId: selectedBook.id,
                reviewText: reviewText
            });
            
            setBooks(books.map(book => 
                book.id === selectedBook.id 
                    ? { ...book, reviews: [...book.reviews, newReview] } 
                    : book
            ));

            closeReviewModal();
        } catch (err) {
            setError('Error al añadir la reseña.');
            console.error(err);
        }
    };

    const handleAddRating = async (e) => {
        e.preventDefault();
        if (rating === 0 || !selectedBook) return;

        try {
            const { data: newRating } = await axios.post('http://localhost:8080/api/ratings', {
                bookId: selectedBook.id,
                userId: 'user123', // Replace with actual user ID
                rating: rating,
                comment: ''
            });

            setBooks(books.map(book => 
                book.id === selectedBook.id 
                    ? { ...book, ratings: [...book.ratings, newRating] } 
                    : book
            ));

            closeRatingModal();
        } catch (err) {
            setError('Error al añadir la calificación.');
            console.error(err);
        }
    };

    const openReviewModal = (book) => {
        setSelectedBook(book);
        setReviewText('');
        setPreviewReview(false);
        setReviewModalOpen(true);
    };

    const closeReviewModal = () => {
        setReviewModalOpen(false);
        setSelectedBook(null);
    };

    const openRatingModal = (book) => {
        setSelectedBook(book);
        setRating(0);
        
        setRatingModalOpen(true);
    };

    const closeRatingModal = () => {
        setRatingModalOpen(false);
        setSelectedBook(null);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setSearchParams(prevState => ({ ...prevState, [name]: value }));
    };

    return (
        <div className="container mt-4">
            <h2>Búsqueda de libros</h2>
            <form onSubmit={handleSearch} className="mb-4">
                <div className="row">
                    <div className="col-md-6">
                        <input
                            type="text"
                            name="term"
                            className="form-control"
                            placeholder="Buscar por título o autor"
                            value={searchParams.term}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-2">
                        <button type="submit" className="btn btn-primary">Buscar</button>
                    </div>
                </div>
            </form>

            <hr />

            <h4>Búsqueda avanzada</h4>
            <form onSubmit={handleSearch} className="mb-4">
                <div className="row">
                    <div className="col-md-4">
                        <input
                            type="text"
                            name="title"
                            className="form-control"
                            placeholder="Buscar por título"
                            value={searchParams.title}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-4">
                        <input
                            type="text"
                            name="author"
                            className="form-control"
                            placeholder="Buscar por autor"
                            value={searchParams.author}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-3">
                        <input
                            type="text"
                            name="isbn"
                            className="form-control"
                            placeholder="Buscar por ISBN"
                            value={searchParams.isbn}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-1">
                        <button type="submit" className="btn btn-success">Buscar</button>
                    </div>
                </div>
            </form>

            {loading && <p>Cargando...</p>}
            {error && <div className="alert alert-danger">{error}</div>}

            <div>
                {books.map(book => (
                    <BookCard key={book.id} book={book} onAddReview={openReviewModal} onAddRating={openRatingModal} />
                ))}
            </div>

            {/* Review Modal */}
            {reviewModalOpen && (
                <div className="modal show d-block" tabIndex="-1">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Añadir reseña para "{selectedBook?.title}"</h5>
                                <button type="button" className="btn-close" onClick={closeReviewModal}></button>
                            </div>
                            <form onSubmit={handleAddReview}>
                                <div className="modal-body">
                                    {!previewReview ? (
                                        <textarea
                                            className="form-control"
                                            rows="3"
                                            placeholder="Escribe tu reseña aquí..."
                                            value={reviewText}
                                            onChange={(e) => setReviewText(e.target.value)}
                                            required
                                        ></textarea>
                                    ) : (
                                        <div>
                                            <h5>Vista previa:</h5>
                                            <p>{reviewText}</p>
                                        </div>
                                    )}
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={closeReviewModal}>Cerrar</button>
                                    {!previewReview ? (
                                        <button type="button" className="btn btn-info" onClick={() => setPreviewReview(true)}>Vista previa</button>
                                    ) : (
                                        <button type="button" className="btn btn-info" onClick={() => setPreviewReview(false)}>Editar</button>
                                    )}
                                    <button type="submit" className="btn btn-primary">Enviar reseña</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}

            {/* Rating Modal */}
            {ratingModalOpen && (
                <div className="modal show d-block" tabIndex="-1">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Añadir calificación para "{selectedBook?.title}"</h5>
                                <button type="button" className="btn-close" onClick={closeRatingModal}></button>
                            </div>
                            <form onSubmit={handleAddRating}>
                                <div className="modal-body">
                                    <div className="mb-3">
                                        <label className="form-label">Calificación</label>
                                        <div>
                                            {[1, 2, 3, 4, 5].map(star => (
                                                <span
                                                    key={star}
                                                    className={star <= rating ? 'text-warning' : 'text-secondary'}
                                                    style={{ cursor: 'pointer', fontSize: '2rem' }}
                                                    onClick={() => setRating(star)}
                                                >
                                                    &#9733;
                                                </span>
                                            ))}
                                        </div>
                                    </div>
                                    
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={closeRatingModal}>Cerrar</button>
                                    <button type="submit" className="btn btn-primary">Enviar calificación</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default BookSearch;
