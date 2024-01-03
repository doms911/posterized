// HomePage.jsx
import Header from './components/Header';
import { Link } from 'react-router-dom';


const HomePage = (props) => {
    const isLoggedIn = props.isLoggedIn;
    const onLogout = props.onLogout;


    return (
        <div className="page-container">
      <Header isLoggedIn={isLoggedIn} onLogout={onLogout} />
      <main className="main-content">
        {isLoggedIn ? (
         <div className="image" style={{ backgroundImage: 'url("https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/headway-F2KRf_QfCqw-unsplash.jpg?alt=media&token=c9dd7219-ea1b-4f47-91f5-da81ec34ebf8")' }}>
         <div className="text">
             <h1>Dobrodošli!</h1>
             <div>Unesite <Link to="/pinInput"> pin</Link> kako biste pristupili konferenciji.
            </div>
         </div>
         </div>
        ) : (
            <div className="image" style={{ backgroundImage: 'url("https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/headway-F2KRf_QfCqw-unsplash.jpg?alt=media&token=c9dd7219-ea1b-4f47-91f5-da81ec34ebf8")' }}>
            <div className="text">
                <h1>Dobrodošli!</h1>
            <div>
            <Link to="/login"> Prijavite se</Link> kako biste pristupili konferencijama i ostalim pojedinostima.
            </div>
            <div>
              Nemate račun? <Link to="/register">Registrirajte se</Link>.
            </div> 
            </div>
            </div>
        )}
      </main>
    </div>
    );
};

export default HomePage;
