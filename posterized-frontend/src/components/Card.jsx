// Card.jsx
import React from 'react';

function Card(props) {
  const { children, title} = props;

  return (
    <div className="Card">
      {title && <h2>{title}</h2>}
      {children}
    </div>
  );
}

export default Card;
