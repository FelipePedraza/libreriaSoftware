
import React, { useState } from 'react';
import axios from 'axios';

// A simple component to display a single book
const BookCard = ({ book, onAddReview }) => (
    <div className="card mb-3">
        <div className="card-body">
            <h5 className="card-title">{book.title}</h5>
            <h6 className="card-subtitle mb-2 text-muted">by {book.author}</h6>
            <p className="card-text"><strong>ISBN:</strong> {book.isbn}</p>
            <div>
                <h6>Reviews:</h6>
                {book.reviews && book.reviews.length > 0 ? (
                    <ul className="list-group list-group-flush">
                        {book.reviews.map(review => (
                            <li key={review.id} className="list-group-item">{review.reviewText}</li>
                        ))}
                    </ul>
                ) : (
                    <p>No reviews yet.</p>
                )}
            </div>
            <button className="btn btn-primary mt-3" onClick={() => onAddReview(book)}>Add Review</button>
        </div>
    </div>
);


const BookSearch = () => {
    const [searchParams, setSearchParams] = useState({ title: '', author: '', isbn: '' });
    const [books, setBooks] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // State for the review modal
    const [reviewModalOpen, setReviewModalOpen] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [reviewText, setReviewText] = useState('');

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
            setError('Failed to fetch books. Make sure the backend is running.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleAddReview = async (e) => {
        e.preventDefault();
        if (!reviewText || !selectedBook) return;

        try {
            await axios.post('http://localhost:8080/api/reviews', {
                bookId: selectedBook.id,
                reviewText: reviewText
            });
            // Refresh the search results to show the new review
            handleSearch(new Event('submit')); 
            closeReviewModal();
        } catch (err) {
            setError('Failed to add review.');
            console.error(err);
        }
    };

    const openReviewModal = (book) => {
        setSelectedBook(book);
        setReviewText('');
        setReviewModalOpen(true);
    };

    const closeReviewModal = () => {
        setReviewModalOpen(false);
        setSelectedBook(null);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setSearchParams(prevState => ({ ...prevState, [name]: value }));
    };

    return (
        <div className="container mt-4">
            <h2>Book Search</h2>
            <form onSubmit={handleSearch} className="mb-4">
                <div className="row">
                    <div className="col-md-4">
                        <input
                            type="text"
                            name="title"
                            className="form-control"
                            placeholder="Search by Title"
                            value={searchParams.title}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-4">
                        <input
                            type="text"
                            name="author"
                            className="form-control"
                            placeholder="Search by Author"
                            value={searchParams.author}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-3">
                        <input
                            type="text"
                            name="isbn"
                            className="form-control"
                            placeholder="Search by ISBN"
                            value={searchParams.isbn}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="col-md-1">
                        <button type="submit" className="btn btn-success">Search</button>
                    </div>
                </div>
            </form>

            {loading && <p>Loading...</p>}
            {error && <div className="alert alert-danger">{error}</div>}

            <div>
                {books.map(book => (
                    <BookCard key={book.id} book={book} onAddReview={openReviewModal} />
                ))}
            </div>

            {/* Review Modal */}
            {reviewModalOpen && (
                <div className="modal show d-block" tabIndex="-1">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Add Review for "{selectedBook?.title}"</h5>
                                <button type="button" className="btn-close" onClick={closeReviewModal}></button>
                            </div>
                            <form onSubmit={handleAddReview}>
                                <div className="modal-body">
                                    <textarea
                                        className="form-control"
                                        rows="3"
                                        placeholder="Write your review here..."
                                        value={reviewText}
                                        onChange={(e) => setReviewText(e.target.value)}
                                        required
                                    ></textarea>
                                </div>
                                <div className="modal-footer">
                                    <button type="button" className="btn btn-secondary" onClick={closeReviewModal}>Close</button>
                                    <button type="submit" className="btn btn-primary">Submit Review</button>
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
