// AdminConference.jsx
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

const AdminConference = () => {
  const { nazivKonferencije } = useParams();
  
  return (
    <div>Konferencija: {nazivKonferencije}</div>
  )
};

export default AdminConference;
