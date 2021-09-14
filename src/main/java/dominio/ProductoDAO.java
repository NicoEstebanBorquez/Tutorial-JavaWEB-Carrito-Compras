package dominio;

import java.util.ArrayList;
import java.util.List;
import datos.Conexion;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

public class ProductoDAO {

    private static final String SQL_SELECT = "SELECT id_producto, nombre, foto, descripcion, precio, stock FROM producto";
    private static final String SQL_SELECT_IMG = "SELECT id_producto, nombre, foto, descripcion, precio, stock FROM producto WHERE id_producto=?";
    private static final String SQL_SELECT_ID = "SELECT id_propietario, nombre, apellidos, dni, pasaporte, nacionalidad, telefono, domicilio, email, id_usuario FROM propietario WHERE id_propietario=?";
    private static final String SQL_INSERT = "INSERT INTO propietario (nombre, apellidos, dni, pasaporte, nacionalidad, telefono, domicilio, email, id_usuario) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE propietario SET nombre=?, apellidos=?, dni=?, pasaporte=?, nacionalidad=?, telefono=?, domicilio=?, email=?, id_usuario=? WHERE id_propietario=?";
    private static final String SQL_DELETE = "DELETE FROM propietario WHERE id_propietario=?";

    public List listar() {

        List<Producto> productos = new ArrayList<>();
        Producto producto = new Producto();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = Conexion.getConnection();
            ps = cn.prepareStatement(SQL_SELECT);
            rs = ps.executeQuery();

            while (rs.next()) {
                producto.setId_producto(rs.getInt(1));
                producto.setNombre(rs.getString(2));
                producto.setFoto(rs.getBinaryStream(3));
                producto.setDescripcion(rs.getString(4));
                producto.setPrecio(rs.getDouble(5));
                producto.setStock(rs.getInt(6));
                productos.add(producto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productos;
    }

    public void listarImg(int id, HttpServletResponse response) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        InputStream iStream = null;
        OutputStream oStream = null;
        BufferedInputStream b_iStream = null;
        BufferedOutputStream b_oStream = null;

        try {
            oStream = response.getOutputStream();
            cn = Conexion.getConnection();
            ps = cn.prepareStatement(SQL_SELECT_IMG);
            rs = ps.executeQuery();

            if (rs.next()) {
                iStream = rs.getBinaryStream("foto");
            }

            b_iStream = new BufferedInputStream(iStream);
            b_oStream = new BufferedOutputStream(oStream);
            int i = 0;
            
            while((i=b_iStream.read()) != -1){
                b_oStream.write(i);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
