// Card.jsx
import React from 'react';
import Header from './Header';

function Card(props) {
  const { children, title, isLoggedIn, onLogout } = props;

  return (
    <div className="Card">
      {title && <h2>{title}</h2>}
      {children}
    </div>
  );
}

export default Card;
