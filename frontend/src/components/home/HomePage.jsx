// import React, { useState } from "react";
// import RoomSearch from "../common/RoomSearch";
// import RoomResults from "../common/RoomResults";

// const HomePage = () => {
//   const [roomSearchResults, setRoomSearchResults] = useState([]);

//   const handleSearchResults = (results) => {
//     setRoomSearchResults(results);
//   };

//   return (
//     <div className="home">
//       {/* Banner Section of homepage */}
//       <section>
//         <header className="header-banner">
//           <img
//             src="./assets/images/banner-image.jpg"
//             alt="Royal Crest"
//             className="banner-img"
//           />
//           <div className="overlay"></div>
//           <div className="animated-texts overlay-content">
//             <h1>
//               Welcome to
//               <span className="royal-crest-color"> Royal Crest</span>
//             </h1>
//             <br />
//             <h3>Step into a heaven of comfort and care</h3>
//           </div>
//         </header>
//       </section>
//       {/* SEARCH/FIND AVAILABLE ROOM SECTION */}
//       <RoomSearch handleSearchResults={handleSearchResults} />
//       <RoomResults roomSearchResults={roomSearchResults} />{" "}
//       {/* sending the room results to RoomResultComponent */}
//       <h4>
//         <a className="view-rooms-home" href="/rooms">
//           All Rooms
//         </a>
//       </h4>
//       <h2 className="home-services">
//         Services at <span className="phegon-color">Phegon Hotel</span>
//       </h2>
//       {/* SERVICES SECTION */}
//       <section className="service-section">
//         <div className="service-card">
//           <img src="./assets/images/ac.png" alt="Air Conditioning" />
//           <div className="service-details">
//             <h3 className="service-title">Air Conditioning</h3>
//             <p className="service-description">
//               Stay cool and comfortable throughout your stay with our
//               individually controlled in-room air conditioning.
//             </p>
//           </div>
//         </div>
//         <div className="service-card">
//           <img src="./assets/images/mini-bar.png" alt="Mini Bar" />
//           <div className="service-details">
//             <h3 className="service-title">Mini Bar</h3>
//             <p className="service-description">
//               Enjoy a convenient selection of beverages and snacks stocked in
//               your room's mini bar with no additional cost.
//             </p>
//           </div>
//         </div>
//         <div className="service-card">
//           <img src="./assets/images/parking.png" alt="Parking" />
//           <div className="service-details">
//             <h3 className="service-title">Parking</h3>
//             <p className="service-description">
//               We offer on-site parking for your convenience . Please inquire
//               about valet parking options if available.
//             </p>
//           </div>
//         </div>
//         <div className="service-card">
//           <img src="./assets/images/wifi.png" alt="WiFi" />
//           <div className="service-details">
//             <h3 className="service-title">WiFi</h3>
//             <p className="service-description">
//               Stay connected throughout your stay with complimentary high-speed
//               Wi-Fi access available in all guest rooms and public areas.
//             </p>
//           </div>
//         </div>
//       </section>
//       {/* AVAILABLE ROOMS SECTION */}
//       <section></section>
//     </div>
//   );
// };

// export default HomePage;
import React, { useState } from "react";
import "./home.css";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
  const navigate = useNavigate();

  const handleBookNowClick = (e) => {
    e.preventDefault();
    navigate("/rooms");
  };
  return (
    <div className="home">
      {/* Banner Section of homepage */}
      <section id="hero-section">
        <div className="container">
          <h2
            style={{
              fontFamily: "cursive",
              color: "coral",
            }}
          >
            Explore
          </h2>
          <h1>Hotel Enthusiast's Haven</h1>
          <p>Every stay is an experience book your next getaway!</p>

          <div id="book-now-button">
            <a
              href="/rooms/book-now"
              id="cruise-login-page"
              onClick={handleBookNowClick}
            >
              Book Now
            </a>
          </div>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
